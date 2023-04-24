package org.reckful.archive.twitter.generator.parts

import kotlinx.html.*
import org.reckful.archive.twitter.generator.AssetLocator
import org.reckful.archive.twitter.generator.util.*
import org.reckful.archive.twitter.generator.util.formatCompact
import org.reckful.archive.twitter.model.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TweetHtmlGenerator(
    private val assetLocator: AssetLocator,
    private val mediaHtmlGenerator: MediaHtmlGenerator
) {
    fun FlowContent.generateTweet(post: Post) {
        div(classes = cssClasses(post)) {
            when (post) {
                is Tweet -> generateTweet(post)
                is ReplyTweet -> generateTweet(post.tweet, post.replyToHandles)
                is RetweetTweet -> generateRetweet(post)
            }
        }
    }

    private fun cssClasses(post: Post): String {
        val classes = buildList {
            add("tweet")
            if (post is Tweet && post.media.isNotEmpty()) {
                add("media-tweet")
            }
            if (post is ReplyTweet) {
                add("reply-tweet")
            }
        }
        return classes.joinToString(separator = " ")
    }

    private fun FlowContent.generateProfilePic(picUrl: String) {
        div(classes = "tweet-profile-pic") {
            img(src = picUrl, alt = "Profile picture") {
                attributes["loading"] = "lazy"
            }
        }
    }

    private fun FlowContent.generateTweet(tweet: Tweet, replyingToHandles: List<String> = emptyList()) {
        generateProfilePic(tweet.user.profilePicUrl)
        div(classes = "tweet-content") {
            generateTweeterInfo(
                name = tweet.user.name,
                handle = tweet.user.handle,
                dateSent = tweet.utcDateTime,
                fromLocation = tweet.location
            )
            replyingToHandles.takeIf { it.isNotEmpty() }?.let {
                generateReplyingToHandlesHeader(it)
            }
            tweet.text.takeIf { it.isNotBlank() }?.let {
                generateTweetText(it, tweet.urls)
            }
            with(mediaHtmlGenerator) {
                generateMediaContent(tweet.media)
            }
            replyingToHandles.takeIf { it.isNotEmpty() }?.let {
                generateShowThisThreadLink(tweet)
            }
            tweet.quote?.let {
                generateQuotedTweet(it)
            }
            generateTweetReactions(retweets = tweet.retweets, likes = tweet.likes)
        }
    }

    private fun FlowContent.generateTweeterInfo(
        name: String? = null,
        handle: String? = null,
        dateSent: ZonedDateTime? = null,
        fromLocation: Location? = null
    ) {
        div(classes = "tweeter-info") {
            name?.let { span(classes = "name") { +it } }
            handle?.let { span(classes = "hashtag") { +"@${it}" } }
            dateSent?.let {
                span(classes = "separator") { +"•" }
                span(classes = "date-sent") { +DATE_SENT_FORMATTER.format(it) }
            }
            fromLocation?.let {
                span(classes = "separator") { +"•" }
                span(classes = "location") {
                    span { +"from ${it.place} " }
                    img(src = "https://flagcdn.com/16x12/${it.countryCode.lowercase()}.png")
                }
            }
        }
    }

    private fun FlowContent.generateReplyingToHandlesHeader(replyingToHandles: List<String>) {
        div(classes = "replying-to") {
            replyingToHandles.forEachIndexed { index, replyHandle ->
                a(href = "https://twitter.com/$replyHandle") {
                    +"@${replyHandle}"
                }
                if (index != replyingToHandles.size - 1) {
                    +", "
                }
            }
        }
    }

    private fun FlowContent.generateTweetText(text: String, knownUrls: List<String>) {
        val textTokens = TextTokenParser.parse(text = text, knownUrls =  knownUrls)
        div(classes = "tweet-text") {
            p {
                textTokens.forEach { token ->
                    when(token) {
                        is TextToken -> +token.text
                        is UrlToken -> {
                            a(href = token.url) {
                                +token.url
                                    .removePrefix("https://")
                                    .removePrefix("http://")
                                    .removePrefix("www.")
                            }
                        }
                        is HandleToken -> {
                            a(href = "https://twitter.com/${token.handle}") {
                                +"@${token.handle}"
                            }
                        }
                    }
                }
            }
        }
    }

    private fun FlowContent.generateShowThisThreadLink(tweet: Tweet) {
        div(classes = "show-thread") {
            a(href = "https://twitter.com/${tweet.user.handle}/status/${tweet.id}") {
                +"Show this thread"
            }
        }
    }

    private fun FlowContent.generateRetweet(retweetTweet: RetweetTweet) {
        div(classes = "retweet") {
            div(classes = "retweeted-block") {
                img(src = assetLocator.locateImage("retweet-bw.svg"), alt = "Retweet icon")
                span(classes = "retweeted-text") {
                    +"${retweetTweet.user.name} Retweeted"
                }
            }
            generateProfilePic("https://pbs.twimg.com/media/EWAJB4WUcAAje8s.png")
            div(classes = "tweet-content") {
                generateTweeterInfo(name = "@${retweetTweet.retweetOfHandle}")
                retweetTweet.retweetOfText.takeIf { it.isNotBlank() }?.let {
                    generateTweetText(it, retweetTweet.retweetUrls)
                }
                with(mediaHtmlGenerator) {
                    generateMediaContent(retweetTweet.retweetOfMedia)
                }
                retweetTweet.quoteWithinRetweet?.let {
                    generateQuotedTweet(it)
                }
            }
        }
    }

    private fun FlowContent.generateQuotedTweet(quote: Quote) {
        div(classes = "quote-tweet") {
            div(classes = "quote-tweet-content") {
                div(classes = "quote-prof-pic-tweeter-info") {
                    span(classes = "name") {
                        +"@${quote.quotedHandle}"
                    }
                }
                if (quote.text.isNotBlank()) {
                    div(classes = "tweet-text") {
                        p {
                            +quote.text
                        }
                    }
                }
                div(classes = "show-thread") {
                    a(href = quote.quotedTweetUrl) {
                        +"Open quoted tweet"
                    }
                }
            }
        }
    }

    private fun FlowContent.generateTweetReactions(retweets: Int, likes: Int) {
        div(classes = "tweet-react") {
            div(classes = "reaction") {
                img(src = assetLocator.locateImage("retweet.svg"), alt = "Retweets") {
                    attributes["loading"] = "lazy"
                }
                span(classes = "react-amount") {
                    +retweets.formatCompact()
                }
            }
            div(classes = "reaction") {
                img(src = assetLocator.locateImage("like.svg"), alt = "Likes") {
                    attributes["loading"] = "lazy"
                }
                span(classes = "react-amount") {
                    +likes.formatCompact()
                }
            }
        }
    }

    private companion object {
        private val DATE_SENT_FORMATTER = DateTimeFormatter.ofPattern("MMM d")
    }
}
