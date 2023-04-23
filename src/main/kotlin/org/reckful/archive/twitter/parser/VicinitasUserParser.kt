package org.reckful.archive.twitter.parser

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class VicinitasUserParser(
    private val delimiter: String = "\t"
) {
    fun parse(file: File): List<VicinitasUser> {
        val lines = file.readLines()
        require(lines.isNotEmpty()) { "Expected the users file \"$file\" to contain data" }

        val header = lines[0]
        verifyHeader(header)

        val data = lines.drop(1)
        return parse(data)
    }

    private fun verifyHeader(headerLine: String) {
        val tokens = headerLine.split(delimiter)
        val expectedColumns = 15
        require(tokens.size == expectedColumns) { "Expected to have $expectedColumns columns in the users file" }
        require(tokens == EXPECTED_HEADERS) { "Expected the users file to have the following columns: $EXPECTED_HEADERS" }
    }

    private fun parse(lines: List<String>): List<VicinitasUser> = lines.map { parse(it) }

    private fun parse(line: String): VicinitasUser {
        val tokens = line.split(delimiter)
        return VicinitasUser(
            userId = tokens[0].toLong(),
            screenName = tokens[1],
            name = tokens[2],
            bio = tokens[3],
            location = tokens[4],
            url = tokens[5],
            followers = tokens[6].toInt(),
            following = tokens[7].toInt(),
            posts = tokens[8].toInt(),
            favorites = tokens[9].toInt(),
            lists = tokens[10].toInt(),
            joined = LocalDateTime.parse(tokens[11], JOIN_DATE_FORMATTER).toLocalDate(),
            verified = tokens[12] == "TRUE",
            protected = tokens[13] == "TRUE",
            defaultProfile = tokens[14] == "TRUE"
        )
    }

    private companion object {
        private val JOIN_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")

        private val EXPECTED_HEADERS: List<String> = listOf(
            "user_id",        // 0
            "screen_name",    // 1
            "name",           // 2
            "bio",            // 3
            "location",       // 4
            "url",            // 5
            "followers",      // 6
            "following",      // 7
            "posts",          // 8
            "favorites",      // 9
            "lists",          // 10
            "created_at",     // 11
            "verified",       // 12
            "protected",      // 13
            "default_profile" // 14
        )
    }
}
