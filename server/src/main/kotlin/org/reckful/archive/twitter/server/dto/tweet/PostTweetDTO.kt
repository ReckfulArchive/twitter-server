package org.reckful.archive.twitter.server.dto.tweet

import org.reckful.archive.twitter.server.dto.*

class PostTweetDTO(
    id: String,
    twitterUrl: String,

    val profileInfo: ShortProfileInfoDTO,
    val dateSent: DateTimeDTO,
    val location: LocationDTO? = null,

    val text: TextDTO? = null,
    val media: List<MediaDTO> = emptyList(),
    val quote: QuoteDTO? = null,

    val reactions: ReactionsDTO
) : TweetDTO(id, twitterUrl) {
    override fun toString(): String {
        return "PostTweetDTO(id=$id, handle=${profileInfo.handle}, text=${text?.plain})"
    }
}
