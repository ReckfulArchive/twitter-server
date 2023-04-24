package org.reckful.archive.twitter.model

import java.time.ZonedDateTime

data class Tweet(
    val id: String,
    val user: User,
    val utcDateTime: ZonedDateTime,
    val source: Source? = null,
    val likes: Int,
    val retweets: Int,
    val quote: Quote? = null,
    val location: Location? = null,
    val media: List<Media> = emptyList(),
    val urls: List<String> = emptyList(),
    val text: String
): Post
