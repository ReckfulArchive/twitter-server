package org.reckful.archive.twitter.server.dto.mapper

import org.reckful.archive.twitter.server.dto.CounterDTO
import org.springframework.stereotype.Component
import java.math.RoundingMode
import java.text.DecimalFormat

@Component
class CounterMapper {

    private val compactDecimalFormat = DecimalFormat("#.#").also {
        it.roundingMode = RoundingMode.DOWN
    }

    fun map(i: Int): CounterDTO {
        return CounterDTO(
            plain = i,
            formatted = formatCompact(i)
        )
    }

    private fun formatCompact(i: Int): String {
        val thousands = i.toDouble() / 1000
        return if (thousands >= 1.0) {
            "${compactDecimalFormat.format(thousands)}K"
        } else {
            i.toString()
        }
    }
}
