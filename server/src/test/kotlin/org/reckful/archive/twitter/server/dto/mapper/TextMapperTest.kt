package org.reckful.archive.twitter.server.dto.mapper

import kotlin.test.Test
import kotlin.test.assertEquals

class TextMapperTest {

    @Test
    fun `should extract the handle in the middle of the text`() {
        testExtractHandles(
            input = "hello @byron how are you",
            expectedHandles = listOf("@byron")
        )
    }

    @Test
    fun `should extract the handle in the beginning of the text`() {
        testExtractHandles(
            input = "@byron how are you",
            expectedHandles = listOf("@byron")
        )
    }

    @Test
    fun `should extract the handle in the end of the text`() {
        testExtractHandles(
            input = "how are you @byron",
            expectedHandles = listOf("@byron")
        )
    }

    @Test
    fun `should extract the handle even if it has special characters after it`() {
        testExtractHandles(
            input = "hello @byron. how are you",
            expectedHandles = listOf("@byron")
        )
    }

    @Test
    fun `should extract multiple handles`() {
        testExtractHandles(
            input = "@LaCookieFreak @BenCHOPP @RajjOfficial was nice to meet you &amp; " +
                    "your brother was hilarious both your personalities are a nice change of pace from what i'm used to",
            expectedHandles = listOf(
                "@LaCookieFreak", "@BenCHOPP", "@RajjOfficial"
            )
        )

        testExtractHandles(
            input = "@RiverboatEcon @derekevxd @ReilsGO @dele_gold @pokimanelol same way you sort through whether " +
                    "they like you for looks or personality. it's always tough",
            expectedHandles = listOf(
                "@RiverboatEcon", "@derekevxd", "@ReilsGO", "@dele_gold", "@pokimanelol"
            )
        )
    }

    @Test
    fun `should not parse an email as a handle`() {
        testExtractHandles(
            input = "was the highest rated player in @Warcraft, now the creator of @PlayEverland. inquiries: reckful@getader.com #blm",
            expectedHandles = listOf(
                "@Warcraft", "@PlayEverland"
            )
        )
    }

    private fun testExtractHandles(input: String, expectedHandles: List<String>) {
        val actualHandles = TextMapper().extractHandleMentions(input)
        assertEquals(expectedHandles.toList(), actualHandles)
    }
}
