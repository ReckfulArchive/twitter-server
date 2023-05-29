package org.reckful.archive.twitter.server.service

import org.reckful.archive.twitter.server.model.tweet.Tweet
import org.reckful.archive.twitter.server.model.tweet.TweetMedia

interface MediaLocatorService {
    fun getUrl(media: TweetMedia, context: Tweet): String

    fun getProfilePictureUrl(handle: String): String

    fun getBannerUrl(handle: String): String
}

