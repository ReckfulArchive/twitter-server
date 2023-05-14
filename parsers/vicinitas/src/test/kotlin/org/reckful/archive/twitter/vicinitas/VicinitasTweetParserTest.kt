package org.reckful.archive.twitter.vicinitas

import kotlin.test.Test
import kotlin.test.assertEquals


class VicinitasTweetParserTest {

    @Test
    fun `should parse tweets without errors`() {
        val testDataFile = loadTestDataFile("vicinitas-test-tweets.tsv")
        val tweets = VicinitasTweetParser(delimiter = "\t")
            .parse(testDataFile)

        assertEquals(8849, tweets.size)
    }
}
