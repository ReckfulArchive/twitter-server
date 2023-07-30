package org.reckful.archive.twitter.server.mapper

import org.reckful.archive.twitter.server.dto.LinkTextTokenDTO
import org.reckful.archive.twitter.server.dto.PlainTextTokenDTO
import org.reckful.archive.twitter.server.dto.TextDTO
import org.reckful.archive.twitter.server.dto.TextTokenDTO
import org.springframework.stereotype.Component

// TODO refactor this when I'm sober, works for now
@Component
class TextMapper {

    fun map(text: String, knownUrls: List<String> = emptyList()): TextDTO {
        val knownHandleMentions = extractHandleMentions(text)
        return TextDTO(
            plain = text,
            tokenized = tokenize(
                text = text,
                knownUrls = knownUrls,
                knownHandleMentions = knownHandleMentions
            ),
            knownUrls = knownUrls,
            knownHandles = knownHandleMentions
        )
    }

    private fun tokenize(text: String, knownUrls: List<String>, knownHandleMentions: List<String>): List<TextTokenDTO> {
        var result: List<TextTokenDTO> = listOf(PlainTextTokenDTO(text))

        knownUrls.forEach { knownUrl ->
            result = enrichWithUrl(result, knownUrl)
        }
        knownHandleMentions.forEach { handleMention ->
            result = enrichWithHandle(result, handleMention)
        }
        return result.filter { it.text.isNotBlank() }
    }

    // internal for testing, private otherwise
    internal fun extractHandleMentions(text: String): List<String> {
        val handleMatcher = HANDLE_MENTION_PATTERN.matcher(text)
        val handles = mutableListOf<String>()
        do {
            val found = handleMatcher.find()
            if (found) {
                handles.add(handleMatcher.group(1).trim())
            }
        } while (found)
        return handles
    }

    private fun enrichWithUrl(tokens: List<TextTokenDTO>, url: String): List<TextTokenDTO> {
        val enrichedTokens = mutableListOf<TextTokenDTO>()
        for (token in tokens) {
            if (token is PlainTextTokenDTO) {
                val text = token.text

                val startIndex = text.indexOf(url)
                if (startIndex == -1) {
                    enrichedTokens.add(token)
                    continue
                }

                val endIndex = startIndex + url.length
                enrichedTokens.add(PlainTextTokenDTO(text.substring(0, startIndex)))
                val linkText = url
                    .removePrefix("https://")
                    .removePrefix("http://")
                    .removePrefix("www.")
                enrichedTokens.add(
                    LinkTextTokenDTO(
                        text = linkText,
                        url = url
                    )
                )
                enrichedTokens.add(PlainTextTokenDTO(text.substring(endIndex, text.length)))
            } else {
                enrichedTokens.add(token)
            }
        }
        return enrichedTokens
    }

    private fun enrichWithHandle(tokens: List<TextTokenDTO>, handleMention: String): List<TextTokenDTO> {
        val enrichedTokens = mutableListOf<TextTokenDTO>()
        for (token in tokens) {
            if (token is PlainTextTokenDTO) {
                val text = token.text
                val startIndex = text.indexOf(handleMention)
                if (startIndex == -1) {
                    enrichedTokens.add(token)
                    continue
                }

                val endIndex = startIndex + handleMention.length
                enrichedTokens.add(PlainTextTokenDTO(text.substring(0, startIndex)))
                enrichedTokens.add(
                    LinkTextTokenDTO(
                        text = handleMention,
                        url = "https://twitter.com/${handleMention.removePrefix("@")}"
                    )
                )
                enrichedTokens.add(PlainTextTokenDTO(text.substring(endIndex, text.length)))
            } else {
                enrichedTokens.add(token)
            }
        }
        return enrichedTokens
    }

    private companion object {
        private val HANDLE_MENTION_PATTERN = "(?<!\\w)(@\\w{1,15})".toRegex().toPattern()
    }
}

