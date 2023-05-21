package org.reckful.archive.twitter.server.controller

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import org.reckful.archive.twitter.server.dto.tweet.TweetDTO
import org.reckful.archive.twitter.server.model.SortOrder
import org.reckful.archive.twitter.server.service.ProfileService
import org.reckful.archive.twitter.server.service.TweetService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/tweets")
class TweetController(
    private val profileService: ProfileService,
    private val tweetService: TweetService
) {

    @GetMapping("/{handle}")
    fun getByHandle(
        @PathVariable("handle") handle: String,
        @RequestParam("type", required = false, defaultValue = "post")
        types: List<String>,

        @RequestParam("only_with_media", required = false, defaultValue = "false")
        onlyWithMedia: Boolean,

        @RequestParam("contains_text", required = false)
        containsText: String?,

        @RequestParam("from_date", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Parameter(schema = Schema(type="string" ,format = "date", example = "2012-02-15"))
        fromDate: LocalDate?,

        @RequestParam("to_date", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Parameter(schema = Schema(type="string" ,format = "date", example = "2020-07-03"))
        toDate: LocalDate?,

        @RequestParam("sort", required = false, defaultValue = "desc")
        sort: String,

        @RequestParam("page", required = false, defaultValue = "0")
        page: Int,

        @RequestParam("limit", required = false, defaultValue = "25")
        limit: Int
    ): List<TweetDTO> {
        val profile = profileService.getByHandle(handle) ?: throw IllegalArgumentException("Unknown handle: $handle")
        val sortOrder = SortOrder.fromString(sort) ?: throw IllegalArgumentException("Unknown sort order: $sort")
        return tweetService.getByProfileHandle(
            profileHandle = profile.handle,
            types = types,
            onlyWithMedia = onlyWithMedia,
            containsText = containsText,
            fromDate = fromDate,
            toDate = toDate,
            sortOrder = sortOrder,
            page = page,
            limit = limit
        )
    }
}
