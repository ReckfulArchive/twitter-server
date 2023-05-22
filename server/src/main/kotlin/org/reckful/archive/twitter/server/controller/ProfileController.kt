package org.reckful.archive.twitter.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.reckful.archive.twitter.server.dto.ProfileDTO
import org.reckful.archive.twitter.server.service.ProfileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/profile")
@Tag(name = "Profiles", description = "API for retrieving profile information")
class ProfileController(
    private val profileService: ProfileService
) {
    @Operation(summary = "Retrieves full details of all known files.")
    @GetMapping("/all", produces = ["application/json"])
    fun getAll(): List<ProfileDTO> {
        return profileService.getAll()
    }

    @Operation(summary = "Retrieves full details of a profile identified by the given handle.")
    @GetMapping("/{handle}", produces = ["application/json"])
    fun getByHandle(
        @Parameter(
            description = "Twitter profile handle, without the at ('@') symbol.",
            schema = Schema(example = "byron")
        )
        @PathVariable("handle") handle: String
    ): ProfileDTO? {
        return profileService.getByHandle(handle)
    }
}
