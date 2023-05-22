package org.reckful.archive.twitter.server.model.tweet

class ReplyTweet(
    val replyToHandles: List<String>,
    val tweet: PostTweet,
) : Tweet(id = tweet.id, userHandle = tweet.userHandle, utcDateTime = tweet.utcDateTime)
