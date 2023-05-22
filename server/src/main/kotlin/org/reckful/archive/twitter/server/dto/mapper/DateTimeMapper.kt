package org.reckful.archive.twitter.server.dto.mapper

import org.reckful.archive.twitter.server.dto.DateTimeDTO
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class DateTimeMapper {

    fun map(localDate: LocalDate): DateTimeDTO {
        return map(localDate.atTime(0, 0, 0))
    }

    fun map(localDateTime: LocalDateTime): DateTimeDTO {
        return DateTimeDTO(
            iso = DateTimeFormatter.ISO_DATE_TIME.format(localDateTime),
            dayOfMonthFormatted = DAY_OF_MONTH_FORMATTER.format(localDateTime),
            monthOfYearFormatted = MONTH_OF_YEAR_FORMATTER.format(localDateTime),
            dateFormatted = DATE_FORMATTER.format(localDateTime),
            dateTimeFormatted = DATE_TIME_FORMATTER.format(localDateTime)
        )
    }

    companion object {
        private val DAY_OF_MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMM d")
        private val MONTH_OF_YEAR_FORMATTER = DateTimeFormatter.ofPattern("MMM yyyy")
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy")
        private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    }
}
