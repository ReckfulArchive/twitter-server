package org.reckful.archive.twitter.server.model.tweet

import java.time.LocalDateTime

class RetweetTweet(
    id: String,
    userHandle: String,
    utcDateTime: LocalDateTime,

    val source: TweetSource? = null,
    val retweetOfHandle: String,
    val retweetOfMedia: List<TweetMedia> = emptyList(),
    val retweetOfText: String,
    val retweetUrls: List<String> = emptyList(),

    val quoteWithinRetweet: TweetQuote? = null
): Tweet(id = id, userHandle = userHandle, utcDateTime = utcDateTime)

