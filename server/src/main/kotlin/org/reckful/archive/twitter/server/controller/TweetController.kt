package org.reckful.archive.twitter.server.controller

import org.reckful.archive.twitter.server.dto.tweet.TweetDTO
import org.reckful.archive.twitter.server.model.SortOrder
import org.reckful.archive.twitter.server.service.ProfileService
import org.reckful.archive.twitter.server.service.TweetService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tweets")
class TweetController(
    private val profileService: ProfileService,
    private val tweetService: TweetService
) {

    @GetMapping("/{handle}")
    fun getByHandle(
        @PathVariable("handle") handle: String,
        @RequestParam("type", required = false, defaultValue = "post") types: List<String>,
        @RequestParam("sort", required = false, defaultValue = "desc") sort: String,
        @RequestParam("page", required = false, defaultValue = "0") page: Int,
        @RequestParam("limit", required = false, defaultValue = "25") limit: Int
    ): List<TweetDTO> {
        val profile = profileService.getByHandle(handle) ?: throw IllegalArgumentException("Unknown handle: $handle")
        val sortOrder = SortOrder.fromString(sort) ?: throw IllegalArgumentException("Unknown sort order: $sort")
        return tweetService.getByProfileHandle(
            profileHandle = profile.handle,
            types = types,
            sortOrder = sortOrder,
            page = page,
            limit = limit
        )
    }
}
