package org.reckful.archive.twitter.model

import java.time.ZonedDateTime

data class RetweetTweet(
    val id: String,
    val user: User,
    val utcDateTime: ZonedDateTime,
    val source: Source? = null,

    val retweetOfHandle: String,
    val retweetOfMedia: List<Media> = emptyList(),
    val retweetOfText: String,
    val retweetUrls: List<String> = emptyList(),

    val quoteWithinRetweet: Quote? = null
): Post
