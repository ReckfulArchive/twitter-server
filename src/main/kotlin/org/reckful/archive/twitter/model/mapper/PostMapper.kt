package org.reckful.archive.twitter.model.mapper

import org.reckful.archive.twitter.model.*
import org.reckful.archive.twitter.parser.VicinitasTweet
import java.time.ZoneId

class PostMapper(
    private val postZoneId: ZoneId,
    users: List<User>,
) {
    private val usersByHandle = users.associateBy { it.handle }

    fun map(vicinitasTweets: List<VicinitasTweet>): List<Post> {
        return vicinitasTweets.map { map(it) }
    }

    private fun map(vicinitasTweet: VicinitasTweet): Post {
        return when (vicinitasTweet.tweetType) {
            "reply" -> mapReply(vicinitasTweet)
            "retweet" -> mapRetweet(vicinitasTweet)
            "tweet" -> mapTweet(vicinitasTweet)
            else -> throw IllegalArgumentException("Unable to parse tweets of type ${vicinitasTweet.tweetType}")
        }
    }

    private fun mapReply(vicinitasTweet: VicinitasTweet): ReplyTweet {
        val (replyHandles, replyText) = deconstructReplyText(vicinitasTweet)
        return ReplyTweet(
            replyToHandles = replyHandles,
            tweet = mapTweet(vicinitasTweet.copy(text = replyText))
        )
    }

    /**
     * @return list of reply handles to reply text
     */
    private fun deconstructReplyText(vicinitasTweet: VicinitasTweet): Pair<List<String>, String> {
        val textTokens = vicinitasTweet.text.split(WHITESPACE_REGEX)

        val handles = mutableListOf<String>()
        for ((index, token) in textTokens.withIndex()) {
            if (token.startsWith("@")) {
                val handle = token.removePrefix("@")
                handles.add(handle)
            } else {
                return handles to textTokens.subList(index, textTokens.size).joinToString(separator = " ")
            }
        }
        error("Unable to parse anything")
    }

    private fun mapRetweet(vicinitasTweet: VicinitasTweet): RetweetTweet {
        val (retweetOfHandle, retweetOfText) = deconstructRetweetText(vicinitasTweet)
        return RetweetTweet(
            id = vicinitasTweet.tweetId,
            user = usersByHandle.getValue(vicinitasTweet.screenName),
            utcDateTime = vicinitasTweet.utcDate.atZone(postZoneId),
            source = getTweetSource(vicinitasTweet),
            retweetOfHandle = retweetOfHandle,
            retweetOfMedia = getMedia(vicinitasTweet),
            retweetOfText = retweetOfText.stripOfLinks(),
            quoteWithinRetweet = getQuote(vicinitasTweet)
        )
    }

    /**
     * @return retweeted handle to retweeted text
     */
    private fun deconstructRetweetText(vicinitasTweet: VicinitasTweet): Pair<String, String> {
        val matcher = RETWEET_TEXT_PATTERN.matcher(vicinitasTweet.text)
        return if (matcher.matches() && matcher.groupCount() == 4) {
            matcher.group(2) to matcher.group(4)
        } else {
            throw IllegalStateException("Unable to extract retweeted handle")
        }
    }

    private fun mapTweet(vicinitasTweet: VicinitasTweet): Tweet {
        return Tweet(
            id = vicinitasTweet.tweetId,
            user = usersByHandle.getValue(vicinitasTweet.screenName),
            utcDateTime = vicinitasTweet.utcDate.atZone(postZoneId),
            source = getTweetSource(vicinitasTweet),
            likes = vicinitasTweet.favorites,
            retweets = vicinitasTweet.retweets,
            quote = getQuote(vicinitasTweet),
            location = getTweetLocation(vicinitasTweet),
            media = getMedia(vicinitasTweet),
            urls = vicinitasTweet.urls,
            text = vicinitasTweet.text.stripOfLinks()
        )
    }

    private fun getTweetSource(vicinitasTweet: VicinitasTweet): Source {
        return when (val source = vicinitasTweet.source) {
            "Twitter Web App", "Twitter Web Client" -> Source.WEB
            "Twitter for iPhone" -> Source.IPHONE
            "Twitter for iPad" -> Source.IPAD
            "iOS", "Camera on iOS" -> Source.IOS
            "Twitter for Android" -> Source.ANDROID
            "Periscope" -> Source.PERISCOPE
            "Vine - Make a Scene" -> Source.VINE
            "Instagram" -> Source.INSTAGRAM
            "TweetDeck" -> Source.TWEET_DECK
            else -> throw IllegalStateException("Unable to map source $source")
        }
    }

    private fun getQuote(vicinitasTweet: VicinitasTweet): Quote? {
        if (vicinitasTweet.quote == null) return null

        val quotedTweetUrl = vicinitasTweet.urls
            .single { it.startsWith("https://twitter.com/") && it.contains("/status/") }

        return Quote(
            quotedTweetUrl = quotedTweetUrl,
            quotedHandle = getHandleFromStatusUrl(quotedTweetUrl),
            text = vicinitasTweet.quote.stripOfLinks()
        )
    }

    private fun getHandleFromStatusUrl(twitterStatusUrl: String): String {
        val statusMatcher = TWITTER_STATUS_URL_PATTERN.matcher(twitterStatusUrl)
        return if (statusMatcher.matches() && statusMatcher.groupCount() == 3) {
            statusMatcher.group(2)
        } else {
            throw IllegalStateException("Unable to match twitter status URL $twitterStatusUrl")
        }
    }

    private fun getTweetLocation(vicinitasTweet: VicinitasTweet): Location? {
        val locationData = listOfNotNull(
            vicinitasTweet.countryCode, vicinitasTweet.place, vicinitasTweet.latitude, vicinitasTweet.longitude
        )

        return if (vicinitasTweet.countryCode == null) {
            check(locationData.isEmpty()) { "Expected all location data to be empty if one is null" }
            null
        } else {
            check(locationData.size == 4) { "Expected all location data to be present if one is present" }
            Location(
                countryCode = checkNotNull(vicinitasTweet.countryCode),
                place = checkNotNull(vicinitasTweet.place),
                latitude = checkNotNull(vicinitasTweet.latitude),
                longitude = checkNotNull(vicinitasTweet.longitude)
            )
        }
    }

    private fun getMedia(vicinitasTweet: VicinitasTweet): List<Media> {
        return vicinitasTweet.media
            .map {
                when (it.type) {
                    "photo" -> PhotoMedia(it.url)
                    "video" -> VideoMedia(it.url)
                    "animated_gif" -> GifMedia(it.url)
                    else -> throw IllegalStateException("Unable to parse media type ${it.type}")
                }
            }
    }

    private fun String.stripOfLinks(): String {
        return this.replace(T_CO_LINK_PATTERN, "")
    }

    companion object {
        private val WHITESPACE_REGEX = "\\s".toRegex()
        private val TWITTER_STATUS_URL_PATTERN = "(https://twitter\\.com/)(.*)(/status/.*)".toRegex().toPattern()
        private val RETWEET_TEXT_PATTERN = "(RT @)(.*)( : )(.*)".toRegex().toPattern()
        private val T_CO_LINK_PATTERN = "https://t.co/\\w+\\s?".toRegex()
    }
}
