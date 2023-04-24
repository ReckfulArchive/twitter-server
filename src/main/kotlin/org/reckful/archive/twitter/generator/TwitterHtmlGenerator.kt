package org.reckful.archive.twitter.generator

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.reckful.archive.twitter.generator.parts.MediaHtmlGenerator
import org.reckful.archive.twitter.generator.parts.ProfileHtmlGenerator
import org.reckful.archive.twitter.generator.parts.TweetHtmlGenerator
import org.reckful.archive.twitter.model.*

private const val MAIN_STYLESHEET = "main.css"
private const val TWEAKS_STYLESHEET = "tweaks.css"
private const val TWEAKS_SCRIPT = "tweaks.js"

data class PageMetadata(
    val title: String,
    val description: String
)

class TwitterHtmlGenerator(
    private val assetLocator: AssetLocator
) {
    private val profileHtmlGenerator = ProfileHtmlGenerator(assetLocator)
    private val tweetHtmlGenerator = TweetHtmlGenerator(assetLocator, MediaHtmlGenerator())

    fun generate(htmlMetadata: PageMetadata, twitterArchive: TwitterArchive): String {
        return createHTML(prettyPrint = true, xhtmlCompatible = false).also {
            it.addHtmlDoctype()
            it.addPageMetadata(htmlMetadata)
            it.addPageBody(twitterArchive)
        }.finalize()
    }

    private fun HtmlPage.addHtmlDoctype() {
        onTagContentUnsafe {
            +"<!DOCTYPE html>"
        }
    }

    private fun HtmlPage.addPageMetadata(htmlMetadata: PageMetadata) {
        head {
            title(htmlMetadata.title)
            meta(charset = "UTF-8")
            meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
            meta(name = "author", content = "Annie Wu, Ignat Beresnev")
            meta(name = "description", content = htmlMetadata.description)

            link(href = "https://fonts.gstatic.com", rel = "preconnect")
            link(href = "https://fonts.googleapis.com/css2?family=Open+Sans&display=swap", rel = "stylesheet")
            link(href = assetLocator.locateCss(MAIN_STYLESHEET), rel = "stylesheet", type = "text/css")
            link(href = assetLocator.locateCss(TWEAKS_STYLESHEET), rel = "stylesheet", type = "text/css")
        }
    }

    private fun HtmlPage.addPageBody(twitterArchive: TwitterArchive) {
        body {
            div(classes = "page") {
                div(classes = "page-content") {
                    with(profileHtmlGenerator) {
                        generateProfileContent(twitterArchive.profile)
                    }
                    generateTweetsContent(twitterArchive.posts)
                    generateFooter()
                }
            }
            script(src = assetLocator.locateJs(TWEAKS_SCRIPT)) {}
        }
    }

    private fun FlowContent.generateTweetsContent(posts: List<Post>) {
        div(classes = "content") {
            posts.forEach {
                with(tweetHtmlGenerator) {
                    generateTweet(it)
                }
            }
        }
    }

    private fun FlowContent.generateFooter() {
        footer(classes = "nav-bar-bottom") {
            a(href = "#") {
                img(src = assetLocator.locateImage("discord-search.svg"), alt = "Search")
            }
        }
    }
}

typealias HtmlPage = TagConsumer<String>
