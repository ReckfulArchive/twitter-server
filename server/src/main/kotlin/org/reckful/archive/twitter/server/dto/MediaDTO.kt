package org.reckful.archive.twitter.server.dto

class MediaDTO(
    val type: String,
    val extension: String,
    val url: String
) {
    constructor(type: Type, extension: String, url: String) : this(type.serializableName, extension, url)

    enum class Type { // NOTE: this is serialized by name as is
        PHOTO, GIF, VIDEO;

        internal val serializableName: String = name
    }
}
