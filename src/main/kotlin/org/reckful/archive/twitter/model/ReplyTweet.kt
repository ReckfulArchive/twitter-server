package org.reckful.archive.twitter.model

data class ReplyTweet(
    val replyToHandles: List<String>,
    val tweet: Tweet
): Post
