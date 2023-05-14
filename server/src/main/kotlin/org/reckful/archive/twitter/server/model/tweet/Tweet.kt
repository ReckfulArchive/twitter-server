package org.reckful.archive.twitter.server.model.tweet

import java.time.LocalDateTime

sealed class Tweet(
    val id: String,
    val userHandle: String,
    val utcDateTime: LocalDateTime
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tweet

        if (id != other.id) return false
        return userHandle == other.userHandle
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userHandle.hashCode()
        return result
    }
}
