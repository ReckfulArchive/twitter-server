package org.reckful.archive.twitter.server.importer.hardcoded

import org.reckful.archive.twitter.server.importer.DataImporter
import org.reckful.archive.twitter.server.model.Profile
import org.reckful.archive.twitter.server.repository.ProfileRepository
import org.reckful.archive.twitter.server.service.MediaLocatorService
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Month

@Component
class HardcodedProfileDataImporter(
    private val profileRepository: ProfileRepository,
    private val mediaLocatorService: MediaLocatorService
) : DataImporter {

    private val byron = Profile(
        id = 492981803L,
        name = "Reckful",
        handle = "Byron",
        description = "was the highest rated player in @Warcraft, now the creator of @PlayEverland. inquiries: reckful@getader.com #blm",
        location = "Austin, TX",
        link = "https://twitch.tv/reckful",
        birthdayDate = LocalDate.of(1989, Month.MAY, 8),
        joinDate = LocalDate.of(2012, Month.FEBRUARY, 15),
        following = 561,
        followers = 379910,
        profilePicUrl = mediaLocatorService.getProfilePictureUrl("byron"),
        bannerUrl = mediaLocatorService.getProfilePictureUrl("byron")
    )

    private val reckful = Profile(
        id = 68562450L,
        name = "reckful",
        handle = "reckful",
        description = "some thoughts. main account @byron",
        location = "Austin, TX",
        link = "https://playeverland.com",
        birthdayDate = LocalDate.of(1989, Month.MAY, 8),
        joinDate = LocalDate.of(2009, Month.AUGUST, 25),
        following = 1,
        followers = 19306,
        profilePicUrl = mediaLocatorService.getProfilePictureUrl("reckful"),
        bannerUrl = mediaLocatorService.getProfilePictureUrl("reckful")

    )

    private val playeverland = Profile(
        id = 1013504130432880641L,
        name = "Everland",
        handle = "PlayEverland",
        description = "Social online world releasing in 2020 — hoping to help people find friendship and a sense of community. Inquiries: dev@playeverland.com",
        location = "Everland",
        link = "https://www.PlayEverland.com",
        birthdayDate = LocalDate.of(2018, Month.JULY, 1),
        joinDate = LocalDate.of(2018, Month.JULY, 1),
        following = 34,
        followers = 25583,
        profilePicUrl = mediaLocatorService.getProfilePictureUrl("playeverland"),
        bannerUrl = mediaLocatorService.getProfilePictureUrl("playeverland")
    )

    override fun import() {
        profileRepository.saveAll(
            listOf(
                byron,
                reckful,
                playeverland,
            )
        )
    }
}
