package org.reckful.archive.twitter.server.service

import org.reckful.archive.twitter.server.dto.*
import org.reckful.archive.twitter.server.dto.mapper.CounterMapper
import org.reckful.archive.twitter.server.dto.mapper.DateTimeMapper
import org.reckful.archive.twitter.server.dto.mapper.TextMapper
import org.reckful.archive.twitter.server.dto.tweet.PostTweetDTO
import org.reckful.archive.twitter.server.dto.tweet.ReplyTweetDTO
import org.reckful.archive.twitter.server.dto.tweet.RetweetTweetDTO
import org.reckful.archive.twitter.server.dto.tweet.TweetDTO
import org.reckful.archive.twitter.server.model.Profile
import org.reckful.archive.twitter.server.model.SortOrder
import org.reckful.archive.twitter.server.model.tweet.*
import org.reckful.archive.twitter.server.repository.ProfileRepository
import org.reckful.archive.twitter.server.repository.TweetQueryParameters
import org.reckful.archive.twitter.server.repository.TweetRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.reflect.KClass

@Service
class TweetService(
    private val tweetRepository: TweetRepository,
    private val profileRepository: ProfileRepository,

    private val dateTimeMapper: DateTimeMapper,
    private val counterMapper: CounterMapper,
    private val tweetTextMapper: TextMapper,
) {
    fun getByProfileHandle(
        profileHandle: String,
        types: List<String>,
        onlyWithMedia: Boolean,
        containsText: String?,
        fromDate: LocalDate?,
        toDate: LocalDate?,
        sortOrder: SortOrder = SortOrder.DESC,
        page: Int,
        limit: Int
    ): List<TweetDTO> {
        val profile = profileRepository.findByHandle(profileHandle) ?: return emptyList()
        val tweets = tweetRepository.findBy(
            TweetQueryParameters(
                profileHandle = profile.handle,
                types = types.map { typeToKClass(it) },
                onlyWithMedia = onlyWithMedia,
                containsText = containsText,
                fromDate = fromDate,
                toDate = toDate,
                sortOrder = sortOrder,
                offset = page * limit,
                limit = limit
            )
        )

        return tweets.map { tweet ->
            when (tweet) {
                is PostTweet -> mapPost(tweet, profile)
                is ReplyTweet -> mapReply(tweet, profile)
                is RetweetTweet -> mapRetweet(tweet, profile)
            }
        }
    }

    private fun typeToKClass(type: String): KClass<out Tweet> {
        return when (type) {
            "post" -> PostTweet::class
            "reply" -> ReplyTweet::class
            "retweet" -> RetweetTweet::class
            else -> throw IllegalArgumentException("Unknown tweet type: $type; expected: post, reply, retweet.")
        }
    }

    private fun mapPost(postTweet: PostTweet, profile: Profile): PostTweetDTO {
        return PostTweetDTO(
            id = postTweet.id,
            twitterUrl = postTweet.getTwitterUrl(),
            profileInfo = profile.toShortInfo(),
            dateSent = dateTimeMapper.map(postTweet.utcDateTime),
            location = postTweet.location?.let {
                LocationDTO(
                    place = it.place,
                    countryCode = it.countryCode,
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            },
            text = tweetTextMapper.map(
                text = postTweet.text,
                knownUrls = postTweet.urls
            ),
            media = postTweet.media.mapIndexed { index, tweetMedia -> tweetMedia.toDTO(index) },
            quote = postTweet.quote?.let { quote ->
                QuoteDTO(
                    tweetUrl = quote.quotedTweetUrl,
                    quotedHandle = quote.quotedHandle,
                    quoteText = tweetTextMapper.map(quote.text)
                )
            },
            reactions = mapReactions(
                likes = postTweet.likes,
                retweets = postTweet.retweets
            )
        )
    }

    private fun mapReply(replyTweet: ReplyTweet, profile: Profile): ReplyTweetDTO {
        val tweet = replyTweet.tweet
        return ReplyTweetDTO(
            id = tweet.id,
            twitterUrl = replyTweet.getTwitterUrl(),
            profileInfo = profile.toShortInfo(),
            dateSent = dateTimeMapper.map(tweet.utcDateTime),
            location = tweet.location?.let {
                LocationDTO(
                    place = it.place,
                    countryCode = it.countryCode,
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            },
            replyToHandles = replyTweet.replyToHandles,
            text = tweetTextMapper.map(tweet.text),
            media = tweet.media.mapIndexed { index, tweetMedia -> tweetMedia.toDTO(index) },
            quote = tweet.quote?.let { quote ->
                QuoteDTO(
                    tweetUrl = quote.quotedTweetUrl,
                    quotedHandle = quote.quotedHandle,
                    quoteText = tweetTextMapper.map(quote.text)
                )
            },
            reactions = mapReactions(
                likes = tweet.likes,
                retweets = tweet.retweets
            )
        )
    }

    private fun Profile.toShortInfo(): ShortProfileInfoDTO {
        return ShortProfileInfoDTO(
            picUrl = this.profilePicUrl,
            name = this.name,
            handle = this.handle
        )
    }

    private fun mapReactions(likes: Int, retweets: Int): ReactionsDTO {
        return ReactionsDTO(
            likes = counterMapper.map(likes),
            retweets = counterMapper.map(retweets),
        )
    }

    private fun mapRetweet(retweetTweet: RetweetTweet, profile: Profile): RetweetTweetDTO {
        return RetweetTweetDTO(
            id = retweetTweet.id,
            twitterUrl = retweetTweet.getTwitterUrl(),
            profileName = profile.name,
            retweetedProfilePicUrl = "TODO",
            retweetedProfileHandle = retweetTweet.retweetOfHandle,
            retweetedText = tweetTextMapper.map(
                text = retweetTweet.retweetOfText,
                knownUrls = retweetTweet.retweetUrls
            ),
            retweetedMedia = retweetTweet.retweetOfMedia.mapIndexed { index, tweetMedia -> tweetMedia.toDTO(index) },
            retweetedQuote = retweetTweet.quoteWithinRetweet?.let { quote ->
                QuoteDTO(
                    tweetUrl = quote.quotedTweetUrl,
                    quotedHandle = quote.quotedHandle,
                    quoteText = tweetTextMapper.map(quote.text)
                )
            }
        )
    }

    private fun Tweet.getTwitterUrl(): String {
        return "https://twitter.com/${this.userHandle}/status/${this.id}"
    }

    private fun TweetMedia.toDTO(index: Int = 0): MediaDTO {
        val extension = getUrlFileType(this.originalUrl)
        return MediaDTO(
            type = when (this) {
                is GifTweetMedia -> MediaDTO.Type.GIF
                is PhotoTweetMedia -> MediaDTO.Type.PHOTO
                is VideoTweetMedia -> MediaDTO.Type.VIDEO
            },
            extension = extension,
            url = getPermanentUrl(index, extension)
        )
    }

    /**
     * @return extension without the dot, like "mp4" or "jpg"
     */
    private fun getUrlFileType(url: String): String {
        return url.substringAfterLast(".").substringBefore("?")
    }

    private fun TweetMedia.getPermanentUrl(index: Int, extension: String): String {
        val dataMediaPath = when (this) {
            is GifTweetMedia -> "gifs/by-tweet-id/${this.tweetId}.mp4"
            is PhotoTweetMedia -> "photos/by-tweet-id/${this.tweetId}-$index.$extension"
            is VideoTweetMedia -> "videos/by-tweet-id/${this.tweetId}.mp4"
        }
        val basePath = "https://reckfularchive.github.io/twitter/data/media"
        return "$basePath/$dataMediaPath"
    }
}
