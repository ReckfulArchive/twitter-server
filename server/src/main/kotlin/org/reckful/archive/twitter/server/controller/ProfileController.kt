package org.reckful.archive.twitter.server.controller

import org.reckful.archive.twitter.server.dto.ProfileDTO
import org.reckful.archive.twitter.server.service.ProfileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/profile")
class ProfileController(
    private val profileService: ProfileService
) {
    @GetMapping("/all")
    fun getAll(): List<ProfileDTO> {
        return profileService.getAll()
    }

    @GetMapping("/{handle}")
    fun getByHandle(@PathVariable("handle") handle: String): ProfileDTO? {
        return profileService.getByHandle(handle)
    }
}
