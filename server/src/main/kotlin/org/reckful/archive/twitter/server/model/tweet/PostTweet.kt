package org.reckful.archive.twitter.server.model.tweet

import org.reckful.archive.twitter.server.model.Location
import java.time.LocalDateTime

class PostTweet(
    id: String,
    userHandle: String,
    utcDateTime: LocalDateTime,

    val source: TweetSource? = null,
    val likes: Int,
    val retweets: Int,
    val quote: TweetQuote? = null,
    val location: Location? = null,
    val media: List<TweetMedia> = emptyList(),
    val urls: List<String> = emptyList(),
    val text: String
): Tweet(id = id, userHandle = userHandle, utcDateTime = utcDateTime)
