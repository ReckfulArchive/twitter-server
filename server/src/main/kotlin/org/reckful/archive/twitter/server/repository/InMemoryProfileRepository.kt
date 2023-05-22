package org.reckful.archive.twitter.server.repository

import org.reckful.archive.twitter.server.model.Profile
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Repository
class InMemoryProfileRepository : ProfileRepository {

    private val handleToProfile: ConcurrentMap<String, Profile> = ConcurrentHashMap()

    override fun findAll(): List<Profile> {
        return handleToProfile.values.toList()
    }

    override fun findByHandle(handle: String): Profile? {
        return handleToProfile[handle.lowercase()]
    }

    override fun saveAll(profiles: List<Profile>) {
        profiles.forEach { profile ->
            handleToProfile[profile.handle.lowercase()] = profile
        }
    }
}
