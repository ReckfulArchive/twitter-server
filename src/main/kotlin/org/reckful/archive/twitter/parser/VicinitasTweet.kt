package org.reckful.archive.twitter.parser

import java.time.LocalDateTime

data class VicinitasTweet(
    val tweetId: String,
    val screenName: String,
    val utcDate: LocalDateTime,
    val source: String,
    val favorites: Int,
    val retweets: Int,
    val lang: String,
    val tweetType: String,
    val quote: String? = null,
    val countryCode: String? = null,
    val place: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val media: List<VicinitasTweetMedia> = emptyList(),
    val urls: List<String> = emptyList(),
    val text: String
)

data class VicinitasTweetMedia(
    val type: String,
    val url: String
)
