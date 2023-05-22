package org.reckful.archive.twitter.server.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "CounterHolder",
    description = "Holder of an int counter that provides multiple convenient representations of the data."
)
data class CounterDTO(
    @Schema(description = "Plain unformatted value", example = "47057")
    val plain: Int,

    @Schema(description = "Formatted value that can be used by a frontend", example = "47K")
    val formatted: String
)
