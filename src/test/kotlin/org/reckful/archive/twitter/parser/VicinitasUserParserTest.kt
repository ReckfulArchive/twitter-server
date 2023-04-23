package org.reckful.archive.twitter.parser

import org.reckful.archive.twitter.loadTestDataFile
import kotlin.test.Test
import kotlin.test.assertEquals


class VicinitasUserParserTest {

    @Test
    fun `should parse users without errors`() {
        val testDataFile = loadTestDataFile("vicinitas-test-users.tsv")
        val users = VicinitasUserParser(delimiter = "\t")
            .parse(testDataFile)

        assertEquals(1, users.size)
    }
}
