package org.reckful.archive.twitter.vicinitas

import org.reckful.archive.twitter.vicinitas.free.TsvVicinitasFreeTweetParser
import org.reckful.archive.twitter.vicinitas.historical.TsvVicinitasHistoricalTweetParser
import kotlin.test.Test
import kotlin.test.assertEquals


class VicinitasTweetParserTest {

    @Test
    fun `should parse historical tweets without errors`() {
        val testDataFile = loadTestDataFile("historical-tweets-byron.tsv")
        val tweets = TsvVicinitasHistoricalTweetParser()
            .parse(testDataFile)

        assertEquals(8849, tweets.size)
    }

    @Test
    fun `should parse free tweets without errors`() {
        val reckfulTweets = TsvVicinitasFreeTweetParser()
            .parse(loadTestDataFile("free-tweets-reckful.tsv"))

        val playeverlandTweets = TsvVicinitasFreeTweetParser()
            .parse(loadTestDataFile("free-tweets-playeverland.tsv"))

        assertEquals(77, reckfulTweets.size)
        assertEquals(20, playeverlandTweets.size)
    }

    @Test
    fun `should substitute a single tco link with the actual value`() {
        val reckfulTweets = TsvVicinitasFreeTweetParser()
            .parse(loadTestDataFile("free-tweets-reckful.tsv"))

        val tweetWithLink = reckfulTweets.single { it.tweetId == "1039945554581745664" }

        val expectedText = """
            last stream with andy+greek. live in 5 http://twitch.tv/reckful
        """.trimIndent()

        assertEquals(expectedText, tweetWithLink.text)
    }

    @Test
    fun `should substitute the first tco link with the actual value, but leave the tco link to photo`() {
        val reckfulTweets = TsvVicinitasFreeTweetParser()
            .parse(loadTestDataFile("free-tweets-reckful.tsv"))

        val tweetWithLink = reckfulTweets.single { it.tweetId == "1039840722436927488" }

        val expectedText = """
            https://clips.twitch.tv/DeliciousAnimatedPepperSwiftRage https://t.co/dErfUWh69I
        """.trimIndent()

        assertEquals(expectedText, tweetWithLink.text)
    }
}
