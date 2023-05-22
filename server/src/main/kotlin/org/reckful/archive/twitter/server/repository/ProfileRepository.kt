package org.reckful.archive.twitter.server.repository

import org.reckful.archive.twitter.server.model.Profile

interface ProfileRepository {
    fun findAll(): List<Profile>
    fun findByHandle(handle: String): Profile?

    fun saveAll(profiles: List<Profile>)
}
