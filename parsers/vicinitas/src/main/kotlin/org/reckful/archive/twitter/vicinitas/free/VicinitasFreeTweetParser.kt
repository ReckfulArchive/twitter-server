package org.reckful.archive.twitter.vicinitas.free

import org.reckful.archive.twitter.vicinitas.VicinitasTweet
import org.reckful.archive.twitter.vicinitas.VicinitasTweetMedia
import org.reckful.archive.twitter.vicinitas.checkNotEmpty
import org.reckful.archive.twitter.vicinitas.takeIfNotBlank
import java.io.File
import java.lang.IllegalStateException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("FunctionName")
fun TsvVicinitasFreeTweetParser() = VicinitasFreeTweetParser(delimiter = "\t")

class VicinitasFreeTweetParser(
    private val delimiter: String
) {
    fun parse(file: File): List<VicinitasTweet> {
        val lines = file.readLines()
        require(lines.isNotEmpty()) { "Expected the tweets file \"$file\" to contain data" }

        val header = lines[0]
        verifyHeader(header)

        val data = lines.drop(1)
        return data.map { parse(it) }
    }

    private fun verifyHeader(headerLine: String) {
        val tokens = headerLine.split(delimiter)
        val expectedColumns = 19
        require(tokens.size == expectedColumns) { "Expected to have $expectedColumns columns in the tweets file" }
        require(tokens == EXPECTED_HEADERS) { "Expected the posts to have the following columns: $EXPECTED_HEADERS" }
    }

    private fun parse(line: String): VicinitasTweet {
        val tokens = line.split(delimiter)
        val urls = parseUrls(tokens)
        return VicinitasTweet(
            tweetId = tokens[0],
            screenName = tokens[3],
            utcDate = LocalDateTime.parse(tokens[4], UTC_DATE_FORMATTER),
            source = parseClient(tokens[9]),
            favorites = tokens[6].toInt(),
            retweets = tokens[7].toInt(),
            lang = tokens[8],
            tweetType = tokens[10],
            quote = null, // seems to be missing from the data
            countryCode = null, // seems to be missing from the data
            place = null, // seems to be missing from the data
            latitude = null, // seems to be missing from the data
            longitude = null, // seems to be missing from the data
            media = parseMedia(tokens),
            urls = urls,
            text = fixTextLinks(tokens[1], urls)
        )
    }

    /**
     * For some reason it includes some HTML, might be a vicinitas' parsing bug
     */
    private fun parseClient(client: String): String {
        return CLIENT_REGEX.find(client)?.groupValues?.get(2)
            ?: throw IllegalStateException("Unknown client string: $client")
    }

    private fun parseMedia(tokens: List<String>): List<VicinitasTweetMedia> {
        val type = takeIfNotBlank(tokens[14]) ?: return emptyList()
        return tokens
            .subList(15, 18)
            .filter { it.isNotBlank() }
            .map { VicinitasTweetMedia(type, checkNotEmpty(it)) }
    }

    private fun parseUrls(tokens: List<String>): List<String> {
        return tokens[11]
            .takeIf { it.isNotBlank() }
            ?.split(WHITESPACE_REGEX)
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }

    /**
     * For some reason the link in text is `t.co`, but we actually know where it's going to lead
     * as this value is stored in a different column. So here we substitute all `t.co` links with
     * their real (non-shortened) url.
     *
     * If multiple t.co links are present, but they are used for different purposes, like
     * one being a proper link and another one being a link to a photo, then the
     * media t.co links are always considered to be the last in the text
     */
    private fun fixTextLinks(text: String, knownUrls: List<String>): String {
        if (knownUrls.isEmpty()) {
            return text
        }

        var urlIndex = 0
        return text.replace(T_CO_HTTPS_LINK_REGEX) { matchingResult ->
            knownUrls.getOrElse(urlIndex++) {
                // probably a link to a photo/video/gif, not a shortened url
                matchingResult.value
            }
        }
    }

    private companion object {
        private val UTC_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        private val WHITESPACE_REGEX = "\\s".toRegex()

        /**
         * Parses strings like `<a href="http://twitter.com/download/iphone" rel="nofollow">Twitter for iPhone</a>`
         *
         * Group 1: `<a href="http://twitter.com/download/iphone" rel="nofollow">`
         * Group 2: `Twitter for iPhone`
         * Group 3: `</a>`
         */
        private val CLIENT_REGEX = "(<a\\shref=\".+\"\\s.+>)(.+)(</a>)".toRegex()

        private val T_CO_HTTPS_LINK_REGEX = "https://t.co/\\w+".toRegex()

        private val EXPECTED_HEADERS: List<String> = listOf(
            "Tweet Id",    // 0
            "Text",        // 1
            "Name",        // 2
            "Screen Name", // 3
            "UTC",         // 4
            "Created At",  // 5
            "Favorites",   // 6
            "Retweets",    // 7
            "Language",    // 8
            "Client",      // 9
            "Tweet Type",  // 10
            "URLs",        // 11
            "Hashtags",    // 12
            "Mentions",    // 13
            "Media Type",  // 14
            "Media URL 1",  // 15
            "Media URL 2",  // 16
            "Media URL 3",  // 17
            "Media URL 4",  // 18
        )
    }

}
