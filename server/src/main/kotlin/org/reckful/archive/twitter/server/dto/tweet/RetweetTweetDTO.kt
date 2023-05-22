package org.reckful.archive.twitter.server.dto.tweet

import io.swagger.v3.oas.annotations.media.Schema
import org.reckful.archive.twitter.server.dto.MediaDTO
import org.reckful.archive.twitter.server.dto.QuoteDTO
import org.reckful.archive.twitter.server.dto.TextDTO

@Schema(
    name = "RetweetTweet",
    description = "A tweet that simply retweets another tweet, without any additional input."
)
class RetweetTweetDTO(
    id: String,
    twitterUrl: String,

    @Schema(description = "Profile name (not handle) of the profile that made the retweet.")
    val profileName: String,

    @Schema(description = "Profile picture URL of the profile being retweeted.")
    val retweetedProfilePicUrl: String,

    @Schema(
        description = "Profile handle of the profile being retweeted, without the at ('@') symbol.",
        example = "reckful"
    )
    val retweetedProfileHandle: String,

    @Schema(description = "Text of the tweet being retweeted.")
    val retweetedText: TextDTO? = null,

    @Schema(description = "Media of the tweet being retweeted.")
    val retweetedMedia: List<MediaDTO> = emptyList(),

    @Schema(description = "Quote within the retweeted tweet.")
    val retweetedQuote: QuoteDTO? = null

) : TweetDTO(id, twitterUrl) {
    override fun toString(): String {
        return "RetweetTweetDTO(id=$id, handle=${retweetedProfileHandle})"
    }
}
