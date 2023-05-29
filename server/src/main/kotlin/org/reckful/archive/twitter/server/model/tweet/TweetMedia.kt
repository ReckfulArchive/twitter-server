package org.reckful.archive.twitter.server.model.tweet

sealed class TweetMedia {
    abstract val tweetId: String

    /**
     * Note: this URL leads to Twitter's servers, and it's not guaranteed to exist for long
     */
    abstract val originalUrl: String
}

data class GifTweetMedia(override val tweetId: String, override val originalUrl: String) : TweetMedia()
data class VideoTweetMedia(override val tweetId: String, override val originalUrl: String) : TweetMedia()
data class PhotoTweetMedia(
    override val tweetId: String,
    override val originalUrl: String,

    /**
     * Index of this photo in a tweet, starting from 0.
     *
     * There can be up to 4 photos in a single tweet.
     */
    val index: Int
) : TweetMedia()
