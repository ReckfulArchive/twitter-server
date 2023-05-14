package org.reckful.archive.twitter.server.dto.tweet

import org.reckful.archive.twitter.server.dto.MediaDTO
import org.reckful.archive.twitter.server.dto.QuoteDTO
import org.reckful.archive.twitter.server.dto.TextDTO

class RetweetTweetDTO(
    id: String,
    twitterUrl: String,

    val profileName: String,

    val retweetedProfileProfilePicUrl: String,
    val retweetedProfileHandle: String,

    val retweetedText: TextDTO? = null,
    val retweetedMedia: List<MediaDTO> = emptyList(),
    val retweetedQuote: QuoteDTO? = null

) : TweetDTO(id, twitterUrl) {
    override fun toString(): String {
        return "RetweetTweetDTO(id=$id, handle=${retweetedProfileHandle})"
    }
}
