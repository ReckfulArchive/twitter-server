package org.reckful.archive.twitter.server.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Media",
    description = "Media file information."
)
class MediaDTO(
    @Schema(description = "Pre-defined type of media.", allowableValues = ["photo", "gif", "video"])
    val type: String,

    @Schema(
        description = "URL at which this media file can be accessed. " +
                "The URL is static and can be used by a user-facing frontend.",
        example = "https://reckfularchive.github.io/twitter/media/byron/photos/1261622132540420096-0.png"
    )
    val url: String
) {
    constructor(type: Type, url: String) : this(type.serializableName, url)

    enum class Type { // NOTE: this is serialized by name as is
        PHOTO, GIF, VIDEO;

        internal val serializableName: String = name.lowercase()
    }
}
