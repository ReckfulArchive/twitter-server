package org.reckful.archive.twitter.server.importer.hardcoded

import org.reckful.archive.twitter.server.importer.DataImporter
import org.reckful.archive.twitter.server.model.Profile
import org.reckful.archive.twitter.server.repository.ProfileRepository
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Month

@Component
class HardcodedProfileDataImporter(
    private val profileRepository: ProfileRepository
) : DataImporter {

    override fun import() {
        profileRepository.saveAll(
            listOf(
                PROFILE_BYRON,
                PROFILE_RECKFUL,
                PROFILE_PLAYEVERLAND,
            )
        )
    }

    private companion object {
        private val PROFILE_BYRON = Profile(
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
            profilePicUrl = "https://reckfularchive.github.io/twitter/html-assets/img/byron-profile-pic.jpg",
            bannerUrl = "https://reckfularchive.github.io/twitter/html-assets/img/byron-banner.png"
        )

        private val PROFILE_RECKFUL = Profile(
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
            profilePicUrl = "https://test.jpg",
            bannerUrl = "https://test.jpg"

        )

        private val PROFILE_PLAYEVERLAND = Profile(
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
            profilePicUrl = "https://test.jpg",
            bannerUrl = "https://test.jpg"
        )
    }
}
