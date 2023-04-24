package org.reckful.archive.twitter.model.mapper

import org.reckful.archive.twitter.model.User
import org.reckful.archive.twitter.parser.VicinitasUser

class UserMapper {

    fun map(vicinitasUsers: List<VicinitasUser>): List<User> {
        return vicinitasUsers.map { mapUser(it) }
    }

    private fun mapUser(vicinitasUser: VicinitasUser): User {
        return User(
            name = vicinitasUser.name,
            handle = vicinitasUser.screenName,
            profilePicUrl = getProfilePicUrl(vicinitasUser)
        )
    }

    private fun getProfilePicUrl(vicinitasUser: VicinitasUser): String = when (vicinitasUser.userId) {
        RECKFUL_USER_ID -> "https://pbs.twimg.com/profile_images/1218183193435746306/stvwX35h_400x400.jpg"
        else -> TODO("Get profile picture from the web or pre-download all of them")
    }

    private companion object {
        private const val RECKFUL_USER_ID = 492981803L
    }
}

