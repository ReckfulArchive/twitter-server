package org.reckful.archive.twitter.server.repository

import org.reckful.archive.twitter.server.model.SortOrder
import org.reckful.archive.twitter.server.model.tweet.PostTweet
import org.reckful.archive.twitter.server.model.tweet.ReplyTweet
import org.reckful.archive.twitter.server.model.tweet.RetweetTweet
import org.reckful.archive.twitter.server.model.tweet.Tweet
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@Repository
class InMemoryTweetRepository : TweetRepository {

    private val handleToTweets: ConcurrentMap<String, List<Tweet>> = ConcurrentHashMap()

    override fun findBy(queryParameters: TweetQueryParameters): List<Tweet> {
        val tweets = handleToTweets[queryParameters.profileHandle.lowercase()] ?: return emptyList()
        return tweets.asSequence()
            .sortedWith(queryParameters.sortOrder)
            .filterByTypes(queryParameters.types)
            .filterOnlyWithMedia(queryParameters.onlyWithMedia)
            .drop(queryParameters.offset)
            .take(queryParameters.limit)
            .toList()
    }

    private fun Sequence<Tweet>.sortedWith(sortOrder: SortOrder): Sequence<Tweet> {
        return when (sortOrder) {
            SortOrder.ASC -> this.sortedBy { it.utcDateTime }
            SortOrder.DESC -> this.sortedByDescending { it.utcDateTime }
        }
    }

    private fun Sequence<Tweet>.filterByTypes(types: List<KClass<out Tweet>>): Sequence<Tweet> {
        return this.filter { tweet ->
            types.any { tweet::class.isSubclassOf(it) }
        }
    }

    private fun Sequence<Tweet>.filterOnlyWithMedia(onlyWithMedia: Boolean): Sequence<Tweet> {
        return if (onlyWithMedia) {
            this.filter { it.hasMedia() }
        } else {
            return this
        }
    }

    private fun Tweet.hasMedia(): Boolean {
        return when(this) {
            is PostTweet -> this.media.isNotEmpty()
            is ReplyTweet -> this.tweet.media.isNotEmpty()
            is RetweetTweet -> this.retweetOfMedia.isNotEmpty()
        }
    }

    override fun saveAll(tweets: List<Tweet>) {
        val tweetsByHandle = tweets.groupBy { it.getAuthorHandle() }
        tweetsByHandle.forEach { (authorHandle, tweets) ->
            save(authorHandle, tweets)
        }
    }

    private fun save(authorHandle: String, tweets: List<Tweet>) {
        handleToTweets.compute(authorHandle.lowercase()) { _, currentTweets ->
            val newTweets = if (currentTweets == null) {
                tweets
            } else {
                currentTweets + tweets
            }

            newTweets.sortedByDescending { it.utcDateTime }
        }
    }

    private fun Tweet.getAuthorHandle(): String {
        return when(this) {
            is PostTweet -> this.userHandle
            is ReplyTweet -> this.tweet.userHandle
            is RetweetTweet -> this.userHandle
        }
    }
}
