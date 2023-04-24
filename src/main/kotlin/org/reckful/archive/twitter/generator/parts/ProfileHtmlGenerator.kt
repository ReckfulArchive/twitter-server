package org.reckful.archive.twitter.generator.parts

import kotlinx.html.*
import org.reckful.archive.twitter.generator.AssetLocator
import org.reckful.archive.twitter.generator.util.formatCompact
import org.reckful.archive.twitter.model.Profile
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfileHtmlGenerator(
    private val assetLocator: AssetLocator
) {
    fun FlowContent.generateProfileContent(profile: Profile) {
        div(classes = "profile-top-container") {
            div(classes = "header-photo") {
                img(src = profile.bannerUrl, alt = "Banner")
            }
            generateProfileData(profile)
            div(classes = "profile-divider")
            generateProfileTweetTabs()
        }
    }

    private fun FlowContent.generateProfileData(profile: Profile) {
        div(classes = "profile") {
            generateBasicProfileData(
                profilePicUrl = profile.profilePicUrl,
                name = profile.name,
                handle = profile.handle,
                bio = profile.description
            )
            div(classes = "location-link-bday-joined") {
                generateLocation(profile.location)
                generateCustomUserLink(profile.link)
                generateBirthdayDate(profile.birthdayDate)
                generateJoinedDate(profile.joinDate)
            }
            generateFollowerNumbers(following = profile.following, followers = profile.followers)
        }
    }

    private fun FlowContent.generateBasicProfileData(
        profilePicUrl: String,
        name: String,
        handle: String,
        bio: String
    ) {
        div(classes = "prof-pic-and-buttons-row") {
            div(classes = "prof-pic") {
                img(src = profilePicUrl, alt = "Profile picture")
            }
        }
        div(classes = "username") {
            span { +name }
            img(src = assetLocator.locateImage("verify.svg"), alt = "Verified icon")
        }
        div(classes = "hashtagNumber") {
            +"@${handle}"
        }
        div(classes = "bio") {
            span { +bio }
        }
    }

    private fun FlowContent.generateLocation(location: String) {
        div(classes = "location") {
            img(src = assetLocator.locateImage("location.svg"), alt = "Location")
            span { +location }
        }
    }

    private fun FlowContent.generateCustomUserLink(link: String) {
        div(classes = "link") {
            img(src = assetLocator.locateImage("link.svg"), alt = "Link")
            span {
                val linkTrimmed = link.removePrefix("http://").removeSuffix("/")
                a(href = link) { +linkTrimmed }
            }
        }
    }

    private fun FlowContent.generateBirthdayDate(birthdayDate: LocalDate) {
        div(classes = "bday") {
            img(src = assetLocator.locateImage("birthday.svg"), alt = "Birthday")
            span { +BIRTHDAY_DATE_FORMATTER.format(birthdayDate) }
        }
    }

    private fun FlowContent.generateJoinedDate(joinedDate: LocalDate) {
        div(classes = "date-joined") {
            img(src = assetLocator.locateImage("calendar.svg"), alt = "Date joined")
            span { +"Joined ${JOINED_DATE_FORMATTER.format(joinedDate)}" }
        }
    }

    private fun FlowContent.generateFollowerNumbers(following: Int, followers: Int) {
        div(classes = "following-follower") {
            div {
                span(classes = "follow-number") { +following.toString() }
                span(classes = "follow-text") { +"Following" }
            }
            div {
                span(classes = "follow-number") { +followers.formatCompact() }
                span(classes = "follow-text") { +"Followers" }
            }
        }
    }

    private fun FlowContent.generateProfileTweetTabs() {
        div(classes = "profile-tweets-tabs") {
            a(href = "javascript:changeTab('tb1', 'tweets');", classes = "tab-button active") {
                id = "tb1"
                +"Tweets"
            }
            a(href = "javascript:changeTab('tb2', 'tweets-with-replies');", classes = "tab-button") {
                id = "tb2"
                +"Tweets & Replies"
            }
            a(href = "javascript:changeTab('tb3', 'media-tweets');", classes = "tab-button") {
                id = "tb3"
                +"Media"
            }
        }
    }

    private companion object {
        private val BIRTHDAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d")
        private val JOINED_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM uuuu")
    }
}
