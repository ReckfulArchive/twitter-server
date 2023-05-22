package org.reckful.archive.twitter.server.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Location",
    description = "Information about a location"
)
class LocationDTO(

    @Schema(description = "Name of the place [unclear what it's defined by]", example = "Austin, TX")
    val place: String,

    @Schema(description = "International country code [seems to be abiding a common spec].", example = "US")
    val countryCode: String,

    @Schema(description = "Latitude", example = "30.3233457")
    val latitude: Double,

    @Schema(description = "Longitude", example = "-97.75472415")
    val longitude: Double,
)
