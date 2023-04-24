package org.reckful.archive.twitter.generator.util

sealed interface Token
data class TextToken(val text: String) : Token
data class UrlToken(val url: String) : Token
data class HandleToken(val handle: String) : Token // handle without @

private val HANDLE_MENTION_PATTERN = "(\\s?@\\w+\\s?)".toRegex().toPattern()

// TODO refactor this when I'm sober, works for now
object TextTokenParser {

    fun parse(text: String, knownUrls: List<String>): List<Token> {
        var result: List<Token> = listOf(TextToken(text))
        val knownHandleMentions = extractHandleMentions(text)

        knownUrls.forEach { knownUrl ->
            result = enrichWithUrl(result, knownUrl)
        }
        knownHandleMentions.forEach { handleMention ->
            result = enrichWithHandle(result, handleMention)
        }
        return result
    }

    private fun extractHandleMentions(text: String): List<String> {
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

    private fun enrichWithUrl(tokens: List<Token>, url: String): List<Token> {
        val enrichedTokens = mutableListOf<Token>()
        for (token in tokens) {
            if (token is TextToken) {
                val text = token.text

                val startIndex = text.indexOf(url)
                if (startIndex == -1) {
                    enrichedTokens.add(token)
                    continue
                }

                val endIndex = startIndex + url.length
                enrichedTokens.add(TextToken(text.substring(0, startIndex)))
                enrichedTokens.add(UrlToken(url))
                enrichedTokens.add(TextToken(text.substring(endIndex, text.length)))
            } else {
                enrichedTokens.add(token)
            }
        }
        return enrichedTokens
    }

    private fun enrichWithHandle(tokens: List<Token>, handleMention: String): List<Token> {
        val enrichedTokens = mutableListOf<Token>()
        for (token in tokens) {
            if (token is TextToken) {
                val text = token.text
                val startIndex = text.indexOf(handleMention)
                if (startIndex == -1) {
                    enrichedTokens.add(token)
                    continue
                }

                val endIndex = startIndex + handleMention.length
                enrichedTokens.add(TextToken(text.substring(0, startIndex)))
                enrichedTokens.add(HandleToken(handleMention.removePrefix("@")))
                enrichedTokens.add(TextToken(text.substring(endIndex, text.length)))
            } else {
                enrichedTokens.add(token)
            }
        }
        return enrichedTokens
    }
}
