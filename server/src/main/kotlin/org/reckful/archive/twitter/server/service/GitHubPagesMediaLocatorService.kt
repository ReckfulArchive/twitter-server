package org.reckful.archive.twitter.server.service

import org.reckful.archive.twitter.server.model.tweet.*
import org.springframework.stereotype.Service

@Service
class GitHubPagesMediaLocatorService : MediaLocatorService {

    override fun getUrl(media: TweetMedia, context: Tweet): String {
        val tweetId = context.id
        val dataMediaPath = when (media) {
            is GifTweetMedia -> "gifs/$tweetId.mp4"
            is PhotoTweetMedia -> {
                val extension = getUrlFileType(media.originalUrl)
                "photos/${tweetId}-${media.index}.$extension"
            }

            is VideoTweetMedia -> "videos/$tweetId.mp4"
        }
        val basePath = "https://reckfularchive.github.io/twitter/media/${context.userHandle.lowercase()}"
        return "$basePath/$dataMediaPath"
    }

    override fun getProfilePictureUrl(handle: String): String {
        return when (handle.lowercase()) {
            "byron" -> "https://reckfularchive.github.io/twitter/media/byron/profile-pic.jpg"
            "reckful" -> "https://reckfularchive.github.io/twitter/media/reckful/profile-pic.jpg"
            "playeverland" -> "https://reckfularchive.github.io/twitter/media/playeverland/profile-pic.jpg"
            else -> "https://reckfularchive.github.io/twitter/media/default-profile-pic.png"
        }
    }

    override fun getBannerUrl(handle: String): String {
        return when (handle.lowercase()) {
            "byron" -> "https://reckfularchive.github.io/twitter/media/byron/banner.png"
            "reckful" -> "https://reckfularchive.github.io/twitter/media/reckful/banner.jpeg"
            "playeverland" -> "https://reckfularchive.github.io/twitter/media/playeverland/banner.jpeg"
            else -> "https://reckfularchive.github.io/twitter/media/default-banner.png"
        }
    }

    /**
     * @return extension without the dot, like "mp4" or "jpg"
     */
    private fun getUrlFileType(url: String): String {
        return url.substringAfterLast(".").substringBefore("?")
    }
}
