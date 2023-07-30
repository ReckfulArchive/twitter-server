package org.reckful.archive.twitter.server.service

import org.reckful.archive.twitter.server.dto.ProfileDTO
import org.reckful.archive.twitter.server.mapper.CounterMapper
import org.reckful.archive.twitter.server.mapper.DateTimeMapper
import org.reckful.archive.twitter.server.mapper.TextMapper
import org.reckful.archive.twitter.server.model.Profile
import org.reckful.archive.twitter.server.repository.ProfileRepository
import org.springframework.stereotype.Service

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,

    private val dateTimeMapper: DateTimeMapper,
    private val textMapper: TextMapper,
    private val counterMapper: CounterMapper
) {
    fun getAll(): List<ProfileDTO> {
        return profileRepository.findAll()
            .map { it.toDTO() }
    }

    /**
     * @param handle profile handle without `at` symbol, any case; example: `byron`
     */
    fun getByHandle(handle: String): ProfileDTO? {
        return profileRepository.findByHandle(handle)
            ?.toDTO()
    }

    private fun Profile.toDTO(): ProfileDTO {
        return ProfileDTO(
            name = this.name,
            handle = this.handle,
            description = textMapper.map(this.description),
            location = this.location,
            link = this.link,
            birthdayDate = dateTimeMapper.map(this.birthdayDate),
            joinDate = dateTimeMapper.map(this.joinDate),
            following = counterMapper.map(this.following),
            followers = counterMapper.map(this.followers),
            profilePicUrl = this.profilePicUrl,
            bannerUrl = this.bannerUrl
        )
    }
}
