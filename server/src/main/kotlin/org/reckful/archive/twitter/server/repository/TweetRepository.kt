package org.reckful.archive.twitter.server.repository

import org.reckful.archive.twitter.server.model.SortOrder
import org.reckful.archive.twitter.server.model.tweet.Tweet
import kotlin.reflect.KClass

interface TweetRepository {
    fun findBy(queryParameters: TweetQueryParameters): List<Tweet>

    fun saveAll(tweets: List<Tweet>)
}

data class TweetQueryParameters(
    val profileHandle: String,
    val types: List<KClass<out Tweet>>,
    val onlyWithMedia: Boolean,
    val containsText: String?,
    val sortOrder: SortOrder,
    val offset: Int,
    val limit: Int,
)
