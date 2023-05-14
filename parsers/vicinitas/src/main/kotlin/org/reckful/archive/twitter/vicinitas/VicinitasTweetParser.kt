package org.reckful.archive.twitter.vicinitas

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("FunctionName")
fun TsvVicinitasTweetParser() = VicinitasTweetParser(delimiter = "\t")

class VicinitasTweetParser(
    private val delimiter: String
) {
    fun parse(file: File): List<VicinitasTweet> {
        val lines = file.readLines()
        require(lines.isNotEmpty()) { "Expected the posts file \"$file\" to contain data" }

        val header = lines[0]
        verifyHeader(header)

        val data = lines.drop(1)
        return parse(data)
    }

    private fun verifyHeader(headerLine: String) {
        val tokens = headerLine.split(delimiter)
        val expectedColumns = 21
        require(tokens.size == expectedColumns) { "Expected to have $expectedColumns columns in the posts file" }
        require(tokens == EXPECTED_HEADERS) { "Expected the posts to have the following columns: $EXPECTED_HEADERS" }
    }

    private fun parse(lines: List<String>): List<VicinitasTweet> = lines.map { parse(it) }

    private fun parse(line: String): VicinitasTweet {
        val tokens = line.split(delimiter)
        return VicinitasTweet(
            tweetId = checkNotEmpty(tokens[0]),
            screenName = checkNotEmpty(tokens[1]),
            utcDate = LocalDateTime.parse(tokens[2], UTC_DATE_FORMATTER),
            source = checkNotEmpty(tokens[4]),
            favorites = checkNotEmpty(tokens[5]).toInt(),
            retweets = checkNotEmpty(tokens[6]).toInt(),
            lang = checkNotEmpty(tokens[7]),
            tweetType = checkNotEmpty(tokens[8]),
            quote = takeNotBlank(tokens[9]),
            countryCode = takeNotBlank(tokens[10]),
            place = takeNotBlank(tokens[11]),
            latitude = takeNotBlank(tokens[12])?.toDouble(),
            longitude = takeNotBlank(tokens[13])?.toDouble(),
            media = parseMedia(tokens),
            urls = parseUrls(tokens),
            text = checkNotEmpty(tokens[20])
        )
    }

    private fun checkNotEmpty(s: String): String = s.also { check(it.isNotEmpty()) }
    private fun takeNotBlank(s: String): String? = s.takeIf { it.isNotBlank() }

    private fun parseMedia(tokens: List<String>): List<VicinitasTweetMedia> {
        val mediaTypes = tokens[14].takeIf { it.isNotBlank() }?.split(WHITESPACE_REGEX) ?: return emptyList()
        return mediaTypes.mapIndexed { index, mediaType ->
            // media1 == 15
            // media2 == 16
            // media3 == 17
            // media4 == 18
            val tokenIndex = 15 + index
            VicinitasTweetMedia(mediaType, checkNotEmpty(tokens[tokenIndex]))
        }
    }

    private fun parseUrls(tokens: List<String>): List<String> {
        return tokens[19]
            .takeIf { it.isNotBlank() }
            ?.split(WHITESPACE_REGEX)
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }

    private companion object {
        private val UTC_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        private val WHITESPACE_REGEX = "\\s".toRegex()

        private val EXPECTED_HEADERS: List<String> = listOf(
            "tweet_id",     // 0
            "screen_name",  // 1
            "utc_date",     // 2
            "uk_time",      // 3
            "source",       // 4
            "favorites",    // 5
            "retweets",     // 6
            "lang",         // 7
            "tweet_type",   // 8
            "quote",        // 9
            "country_code", // 10
            "place",        // 11
            "latitude",     // 12
            "longitude",    // 13
            "media_type",   // 14
            "media1",       // 15
            "media2",       // 16
            "media3",       // 17
            "media4",       // 18
            "urls",         // 19
            "text"          // 20
        )
    }
}
