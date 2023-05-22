package org.reckful.archive.twitter.server.dto.tweet

import io.swagger.v3.oas.annotations.media.Schema
import org.reckful.archive.twitter.server.dto.*

@Schema(
    name = "PostTweet",
    description = "Tweet of type post represents the default and generic tweet. " +
            "It's not a reply and not a retweet, but it can quote other tweets."
)
class PostTweetDTO(
    id: String,
    twitterUrl: String,

    @Schema(description = "Some information about the tweet sender. Always present.")
    val profileInfo: ShortProfileInfoDTO,

    @Schema(description = "Date and time at which this tweet was sent, UTC. Always present.")
    val dateSent: DateTimeDTO,

    @Schema(description = "Location at which this tweet was sent.")
    val location: LocationDTO? = null,

    @Schema(description = "Text of the tweet, if any.")
    val text: TextDTO? = null,

    @Schema(
        description = "Any media attached to the tweet, such as photos/gif/video. " +
                "In a single tweet there can be: 1-3 photos OR 1 gif OR 1 video."
    )
    val media: List<MediaDTO> = emptyList(),

    @Schema(description = "Information about the tweet this tweet is quoting.")
    val quote: QuoteDTO? = null,

    @Schema(description = "Reaction counters, such as likes and retweets.")
    val reactions: ReactionsDTO

) : TweetDTO(id, twitterUrl) {
    override fun toString(): String {
        return "PostTweetDTO(id=$id, handle=${profileInfo.handle}, text=${text?.plain})"
    }
}
