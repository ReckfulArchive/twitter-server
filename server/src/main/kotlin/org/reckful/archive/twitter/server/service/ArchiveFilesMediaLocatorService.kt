package org.reckful.archive.twitter.server.service

import org.reckful.archive.twitter.server.model.tweet.*
import org.springframework.stereotype.Service

private const val FILES_PATH = "https://files.reckful-archive.org/twitter"

private const val DEFAULT_PROFILE_PIC =
    "https://raw.githubusercontent.com/ReckfulArchive/twitter-website/master/assets/img/default-profile-pic.png"

private const val DEFAULT_BANNER =
    "https://raw.githubusercontent.com/ReckfulArchive/twitter-website/master/assets/img/default-banner.png"

@Service
class ArchiveFilesMediaLocatorService : MediaLocatorService {

    override fun getUrl(media: TweetMedia, context: Tweet): String {
        val tweetId = context.id
        val mediaPath = when (media) {
            is GifTweetMedia -> "gifs/by-tweet-id/$tweetId.mp4"
            is PhotoTweetMedia -> {
                val extension = getUrlFileType(media.originalUrl)
                "photos/${tweetId}-${media.index}.$extension"
            }

            is VideoTweetMedia -> "videos/by-tweet-id/$tweetId.mp4"
        }
        return "$FILES_PATH/${context.userHandle.lowercase()}/media/$mediaPath"
    }

    override fun getProfilePictureUrl(handle: String): String {
        return when (handle.lowercase()) {
            "byron" -> "$FILES_PATH/byron/media/profile-pic.jpg"
            "playeverland" -> "$FILES_PATH/playeverland/media/profile-pic.jpg"
            "reckful" -> "$FILES_PATH/reckful/media/profile-pic.jpg"
            else -> DEFAULT_PROFILE_PIC
        }
    }

    override fun getBannerUrl(handle: String): String {
        return when (handle.lowercase()) {
            "byron" -> "$FILES_PATH/byron/media/banner.png"
            "playeverland" -> "$FILES_PATH/playeverland/media/banner.jpeg"
            "reckful" -> "$FILES_PATH/reckful/media/banner.jpeg"
            else -> DEFAULT_BANNER
        }
    }

    /**
     * @return extension without the dot, like "mp4" or "jpg"
     */
    private fun getUrlFileType(url: String): String {
        return url.substringAfterLast(".").substringBefore("?")
    }
}
