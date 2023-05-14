package org.reckful.archive.twitter.vicinitas

import java.time.LocalDate

data class VicinitasUser(
    val userId: Long,
    val screenName: String,
    val name: String,
    val bio: String,
    val location: String,
    val url: String,
    val followers: Int,
    val following: Int,
    val posts: Int,
    val favorites: Int,
    val lists: Int,
    val joined: LocalDate,
    val verified: Boolean,
    val protected: Boolean,
    val defaultProfile: Boolean
)
