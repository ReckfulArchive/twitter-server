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
        profileRepository.saveAll(listOf(
            PROFILE_BYRON
        ))
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
    }
}
