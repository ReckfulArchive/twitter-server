package org.reckful.archive.twitter.server.dto.tweet

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    Type(value = PostTweetDTO::class, name = "post"),
    Type(value = ReplyTweetDTO::class, name = "reply"),
    Type(value = RetweetTweetDTO::class, name = "retweet"),
)
@Schema(
    name = "BaseTweet",
    description = "Base information that exists in all types of tweets."
)
abstract class TweetDTO(

    @Schema(
        description = "Unique tweet identifier, same as the one in the twitter url.",
        example = "1228930684082966528"
    )
    val id: String,

    @Schema(
        description = "Twitter URL of this tweet, can be used to find the original.",
        example = "https://twitter.com/Byron/status/1228930684082966528"
    )
    val twitterUrl: String
)

