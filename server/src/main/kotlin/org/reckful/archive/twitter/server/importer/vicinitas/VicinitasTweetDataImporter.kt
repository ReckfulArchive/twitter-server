package org.reckful.archive.twitter.server.importer.vicinitas

import org.reckful.archive.twitter.server.importer.DataImporter
import org.reckful.archive.twitter.server.model.*
import org.reckful.archive.twitter.server.model.tweet.*
import org.reckful.archive.twitter.server.repository.TweetRepository
import org.reckful.archive.twitter.vicinitas.TsvVicinitasTweetParser
import org.reckful.archive.twitter.vicinitas.VicinitasTweet
import org.springframework.stereotype.Component

@Component
class VicinitasTweetDataImporter(
    private val vicinitasDataConfiguration: VicinitasTweetDataConfiguration,
    private val tweetRepository: TweetRepository
) : DataImporter {

    override fun import() {
        tweetRepository.saveAll(parseTweets())
    }

    private fun parseTweets(): List<Tweet> {
        val tsvTweetParser = TsvVicinitasTweetParser()
        return vicinitasDataConfiguration.tweetsTsv.flatMap { tsvData ->
            val tweets = tsvTweetParser.parse(tsvData)
            tweets.map { map(it) }
        }
    }

    private fun map(vicinitasTweet: VicinitasTweet): Tweet {
        return when (vicinitasTweet.tweetType) {
            "reply" -> mapReply(vicinitasTweet)
            "retweet" -> mapRetweet(vicinitasTweet)
            "tweet" -> mapTweet(vicinitasTweet)
            else -> throw IllegalArgumentException("Unable to parse tweets of type ${vicinitasTweet.tweetType}")
        }
    }

    private fun mapReply(vicinitasTweet: VicinitasTweet): ReplyTweet {
        val (replyHandles, replyText) = deconstructReplyText(vicinitasTweet)

        // if there are no reply handles in the tweet, it means the user is replying to itself (i.e it's a thread)
        val replyToHandles = replyHandles.takeIf { it.isNotEmpty() } ?: listOf(vicinitasTweet.screenName)
        return ReplyTweet(
            replyToHandles = replyToHandles,
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
                val replyText = textTokens
                    .subList(index, textTokens.size)
                    .joinToString(separator = " ")

                return handles to replyText
            }
        }
        error("Unable to parse anything")
    }

    private fun mapRetweet(vicinitasTweet: VicinitasTweet): RetweetTweet {
        val (retweetOfHandle, retweetOfText) = deconstructRetweetText(vicinitasTweet)
        return RetweetTweet(
            id = vicinitasTweet.tweetId,
            userHandle = vicinitasTweet.screenName,
            utcDateTime = vicinitasTweet.utcDate,
            source = getTweetSource(vicinitasTweet),
            retweetOfHandle = retweetOfHandle,
            retweetOfMedia = getMedia(vicinitasTweet),
            retweetOfText = retweetOfText.stripText(),
            retweetUrls = vicinitasTweet.urls,
            quoteWithinRetweet = getQuote(vicinitasTweet)
        )
    }

    /**
     * @return retweeted handle to retweeted text
     */
    private fun deconstructRetweetText(vicinitasTweet: VicinitasTweet): Pair<String, String> {
        val matcher = RETWEET_TEXT_PATTERN.matcher(vicinitasTweet.text)
        return if (matcher.matches() && matcher.groupCount() == 4) {
            val retweetedHandle = matcher.group(2)
            val retweetedText = matcher.group(4)
            retweetedHandle to retweetedText
        } else {
            throw IllegalStateException("Unable to extract retweeted handle")
        }
    }

    private fun mapTweet(vicinitasTweet: VicinitasTweet): PostTweet {
        return PostTweet(
            id = vicinitasTweet.tweetId,
            userHandle = vicinitasTweet.screenName,
            utcDateTime = vicinitasTweet.utcDate,
            source = getTweetSource(vicinitasTweet),
            likes = vicinitasTweet.favorites,
            retweets = vicinitasTweet.retweets,
            quote = getQuote(vicinitasTweet),
            location = getTweetLocation(vicinitasTweet),
            media = getMedia(vicinitasTweet),
            urls = vicinitasTweet.urls,
            text = vicinitasTweet.text.stripText()
        )
    }

    private fun getTweetSource(vicinitasTweet: VicinitasTweet): TweetSource {
        return when (val source = vicinitasTweet.source) {
            "Twitter Web App", "Twitter Web Client" -> TweetSource.WEB
            "Twitter for iPhone" -> TweetSource.IPHONE
            "Twitter for iPad" -> TweetSource.IPAD
            "iOS", "Camera on iOS" -> TweetSource.IOS
            "Twitter for Android" -> TweetSource.ANDROID
            "Periscope" -> TweetSource.PERISCOPE
            "Vine - Make a Scene" -> TweetSource.VINE
            "Instagram" -> TweetSource.INSTAGRAM
            "TweetDeck" -> TweetSource.TWEET_DECK
            else -> throw IllegalStateException("Unable to map source $source")
        }
    }

    private fun getQuote(vicinitasTweet: VicinitasTweet): TweetQuote? {
        val quote = vicinitasTweet.quote ?: return null
        val quotedTweetUrl = vicinitasTweet.urls
            .single { it.startsWith("https://twitter.com/") && it.contains("/status/") }

        return TweetQuote(
            quotedTweetUrl = quotedTweetUrl,
            quotedHandle = getHandleFromStatusUrl(quotedTweetUrl),
            text = quote.stripText()
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

    private fun getMedia(vicinitasTweet: VicinitasTweet): List<TweetMedia> {
        return vicinitasTweet.media
            .map {
                when (it.type) {
                    "photo" -> PhotoTweetMedia(vicinitasTweet.tweetId, it.url)
                    "video" -> VideoTweetMedia(vicinitasTweet.tweetId, it.url)
                    "animated_gif" -> GifTweetMedia(vicinitasTweet.tweetId, it.url)
                    else -> throw IllegalStateException("Unable to parse media type ${it.type}")
                }
            }
    }

    private fun String.stripText(): String {
        return this
            .replace(T_CO_HTTP_LINK_REGEX, "")
            .replace(T_CO_HTTPS_LINK_REGEX, "")
            .trim()
    }

    companion object {
        private val WHITESPACE_REGEX = "\\s".toRegex()

        /**
         * Parser the long twitter.com status link.
         *
         * Example:
         * > https://twitter.com/Byron/status/1278678249334353921
         *
         * Three groups total, the most useful ones are:
         * * Group 2: user handle
         */
        private val TWITTER_STATUS_URL_PATTERN = "(https://twitter\\.com/)(.*)(/status/.*)".toRegex().toPattern()

        /**
         * Parsers the text field of retweets, just a quirk of data from vicinitas.
         *
         * Example:
         * > RT @naval : The world is full of teachers but devoid of students.
         *
         * Has four groups total, and the most useful ones are:
         * * Group 2: handle of the person being retweeted
         * * Group 4: text of the retweeted tweet
         */
        private val RETWEET_TEXT_PATTERN = "(RT @)(.*)( : )(.*)".toRegex().toPattern()

        /**
         * Vicinitas data includes these links to original tweets in their
         * text for some reason, but they are just noise and can be removed.
         */
        // TODO combine into a single regex
        private val T_CO_HTTP_LINK_REGEX = "http://t.co/\\w+\\s?".toRegex()
        private val T_CO_HTTPS_LINK_REGEX = "https://t.co/\\w+\\s?".toRegex()
    }
}
