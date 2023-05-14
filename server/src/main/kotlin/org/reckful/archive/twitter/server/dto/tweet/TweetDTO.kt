package org.reckful.archive.twitter.server.dto.tweet

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    Type(value = PostTweetDTO::class, name = "post"),
    Type(value = ReplyTweetDTO::class, name = "reply"),
    Type(value = RetweetTweetDTO::class, name = "retweet"),
)
abstract class TweetDTO(
    val id: String,
    val twitterUrl: String
)

