package org.reckful.archive.twitter.model

import java.time.LocalDate

data class Profile(
    val id: Long,
    val name: String,
    val handle: String,
    val description: String,
    val location: String,
    val link: String,
    val birthdayDate: LocalDate,
    val joinDate: LocalDate,
    val following: Int,
    val followers: Int,
    val profilePicUrl: String,
    val bannerUrl: String
)
