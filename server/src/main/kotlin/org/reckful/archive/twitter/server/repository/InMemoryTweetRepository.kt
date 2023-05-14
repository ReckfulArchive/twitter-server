package org.reckful.archive.twitter.server.repository

import org.reckful.archive.twitter.server.model.tweet.PostTweet
import org.reckful.archive.twitter.server.model.tweet.ReplyTweet
import org.reckful.archive.twitter.server.model.tweet.RetweetTweet
import org.reckful.archive.twitter.server.model.tweet.Tweet
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Repository
class InMemoryTweetRepository : TweetRepository {

    private val handleToTweets: ConcurrentMap<String, List<Tweet>> = ConcurrentHashMap()

    override fun findBy(queryParameters: TweetQueryParameters): List<Tweet> {
        val tweets = handleToTweets[queryParameters.profileHandle.lowercase()] ?: emptyList()
        return tweets.subList(
            fromIndex = queryParameters.offset,
            toIndex = minOf(queryParameters.offset + queryParameters.limit, tweets.size)
        )
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
