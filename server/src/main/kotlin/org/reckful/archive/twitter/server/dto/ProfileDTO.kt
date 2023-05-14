package org.reckful.archive.twitter.server.dto

data class ProfileDTO(
    val name: String,
    val handle: String,
    val description: TextDTO,
    val location: String,
    val link: String,
    val birthdayDate: DateTimeDTO,
    val joinDate: DateTimeDTO,
    val following: CounterDTO,
    val followers: CounterDTO,
    val profilePicUrl: String,
    val bannerUrl: String
)

