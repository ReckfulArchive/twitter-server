package org.reckful.archive.twitter.server.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Profile", description = "Information about a profile.")
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

