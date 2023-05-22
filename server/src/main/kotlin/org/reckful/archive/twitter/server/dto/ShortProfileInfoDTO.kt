package org.reckful.archive.twitter.server.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "ShortProfileInfo",
    description = "Some basic information about a profile. For full information, query the profile separately."
)
class ShortProfileInfoDTO(
    @Schema(
        description = "Profile picture URL. The URL is static and can be used by a user-facing frontend.",
        example = "https://reckfularchive.github.io/twitter/html-assets/img/byron-profile-pic.jpg"
    )
    val picUrl: String,

    @Schema(description = "Public name of the profile (not to be confused with the handle)", example = "Reckful")
    val name: String,

    @Schema(
        description = "Handle of the profile (not to be confused with the name), without the at ('@') symbol.",
        example = "Byron"
    )
    val handle: String
)
