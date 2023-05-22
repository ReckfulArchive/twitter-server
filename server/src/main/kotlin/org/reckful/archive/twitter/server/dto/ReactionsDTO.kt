package org.reckful.archive.twitter.server.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "ReactionsHolder",
    description = "Holder of reaction values, such as likes and retweets."
)
class ReactionsDTO(
    @Schema(description = "Number of likes.")
    val likes: CounterDTO,

    @Schema(description = "Number of retweets.")
    val retweets: CounterDTO,
)
