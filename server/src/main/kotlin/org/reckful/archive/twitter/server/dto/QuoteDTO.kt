package org.reckful.archive.twitter.server.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Quote",
    description = "Information about a tweet being quoted."
)
class QuoteDTO(
    @Schema(
        description = "Twitter URL of the quoted tweet, can be used to find the original.",
        example = "https://twitter.com/Byron/status/1228930684082966528"
    )
    val tweetUrl: String,

    @Schema(description = "Handle of the profile being quoted, without the at ('@') symbol.", example = "reckful")
    val quotedHandle: String,

    @Schema(description = "Text of the tweet being quoted.")
    val quoteText: TextDTO
)
