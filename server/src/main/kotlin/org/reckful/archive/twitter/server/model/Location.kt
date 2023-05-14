package org.reckful.archive.twitter.server.model

data class Location(
    val countryCode: String,
    val place: String,
    val latitude: Double,
    val longitude: Double,
)
