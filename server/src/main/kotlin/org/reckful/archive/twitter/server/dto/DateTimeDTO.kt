package org.reckful.archive.twitter.server.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "DateTimeHolder",
    description = "Holder of a date-time value that provides multiple convenient representations of the data."
)
class DateTimeDTO(
    @Schema(description = "Date-time in the format defined by ISO 8601", example = "2020-07-02T13:04:17")
    val iso: String,

    @Schema(description = "Compactly formatted date that contains only the day and the month", example = "Jul 2")
    val dayOfMonthFormatted: String,

    @Schema(description = "Formatted date that contains only the month and the year", example = "Jul 2020")
    val monthOfYearFormatted: String,

    @Schema(description = "Formatted date", example = "Jul 2, 2020")
    val dateFormatted: String,

    @Schema(description = "Formatted date-time", example = "02.07.2020 13:04:17")
    val dateTimeFormatted: String
)
