package org.reckful.archive.twitter.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.reckful.archive.twitter.server.dto.tweet.TweetDTO
import org.reckful.archive.twitter.server.model.SortOrder
import org.reckful.archive.twitter.server.service.ProfileService
import org.reckful.archive.twitter.server.service.TweetService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@RestController
@RequestMapping("/tweets")
@Tag(name = "Tweets", description = "API for retrieving tweets")
class TweetController(
    private val profileService: ProfileService,
    private val tweetService: TweetService
) {

    @Operation(summary = "Retrieves tweets of a profile by handle")
    @ApiResponse(responseCode = "404", description = "No profile exists with the given handle")
    @GetMapping("/{handle}", produces = ["application/json"])
    fun getByHandle(
        @PathVariable("handle")
        @Parameter(
            description = "Twitter profile handle, without the at ('@') symbol.",
            schema = Schema(example = "byron")
        )
        handle: String,

        @RequestParam("type", required = false, defaultValue = "post")
        @Parameter(
            description = "Types of tweets to retrieve: post, reply, retweet. By default, only posts are returned.",
            schema = Schema(
                type = "array",
                allowableValues = ["post", "reply", "retweet"],
                pattern = "^(post|reply|retweet)\$"
            )
        )
        types: List<String>,

        @RequestParam("only_with_media", required = false, defaultValue = "false")
        @Parameter(description = "Whether only tweets that contains media (photo/gif/video) should be returned.")
        onlyWithMedia: Boolean,

        @RequestParam("contains_text", required = false)
        @Parameter(description = "Substring to be searched for in tweet and quote texts.")
        containsText: String?,

        @RequestParam("from_date", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Parameter(
            description = "Retrieve tweets starting from the given date, inclusive. " +
                    "The accepted date format is defined by ISO 8601. Example: 2012-02-15.",
            schema = Schema(type = "string", format = "date", pattern = "^\\d{4}-\\d{2}-\\d{2}\$")
        )
        fromDate: LocalDate?,

        @RequestParam("to_date", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Parameter(
            description = "Retrieve tweets starting from the given date, inclusive. " +
                    "The accepted date format is defined by ISO 8601. Example: 2020-07-15",
            schema = Schema(type = "string", format = "date", pattern = "^\\d{4}-\\d{2}-\\d{2}\$")
        )
        toDate: LocalDate?,

        @RequestParam("sort", required = false, defaultValue = "desc")
        @Parameter(
            description = "Sorting order. Tweets are sorted by date-time.",
            schema = Schema(allowableValues = ["asc", "desc"], defaultValue = "desc")
        )
        sort: String,

        @RequestParam("page", required = false, defaultValue = "0")
        @Parameter(
            description = "Zero-based page index (0..N)",
            schema = Schema(type = "integer", minimum = "0", defaultValue = "0")
        )
        page: Int,

        @RequestParam("size", required = false, defaultValue = "25")
        @Parameter(
            description = "The size of the page to be returned.",
            schema = Schema(type = "integer", minimum = "1", defaultValue = "25")
        )
        size: Int
    ): List<TweetDTO> {
        val profile = profileService.getByHandle(handle)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Profile with handle \"$handle\" not found")

        val sortOrder = SortOrder.fromString(sort) ?: throw IllegalArgumentException("Unknown sort order: $sort")
        return tweetService.getByProfileHandle(
            profileHandle = profile.handle,
            types = types.distinct(),
            onlyWithMedia = onlyWithMedia,
            containsText = containsText,
            fromDate = fromDate,
            toDate = toDate,
            sortOrder = sortOrder,
            page = page,
            limit = size
        )
    }
}
