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

abstract class Media(
    val url: String
)

class PhotoMedia(url: String) : Media(url)
class GifMedia(url: String) : Media(url)
class VideoMedia(url: String) : Media(url)
