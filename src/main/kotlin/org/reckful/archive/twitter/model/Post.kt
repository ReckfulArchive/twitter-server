package org.reckful.archive.twitter.model

sealed interface Post

enum class Source {
    WEB,
    IPHONE,
    IPAD,
    IOS,
    ANDROID,
    PERISCOPE,
    VINE,
    INSTAGRAM,
    TWEET_DECK,
}

data class Quote(
    val quotedTweetUrl: String,
    val quotedHandle: String,
    val text: String
)

data class Location(
    val countryCode: String,
    val place: String,
    val latitude: Double,
    val longitude: Double,
)

abstract class Media {
    abstract val url: String
}

data class PhotoMedia(override val url: String) : Media()
data class GifMedia(override val url: String) : Media()
data class VideoMedia(override val url: String) : Media()
