package org.reckful.archive.twitter.server.repository

import org.reckful.archive.twitter.server.model.Profile
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.Month

interface ProfileRepository {
    fun findAll(): List<Profile>
    fun findByHandle(handle: String): Profile?

    fun saveAll(profiles: List<Profile>)
}
