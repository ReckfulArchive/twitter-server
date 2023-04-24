package org.reckful.archive.twitter.generator.util

import java.math.RoundingMode
import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#.#").also {
    it.roundingMode = RoundingMode.DOWN
}

internal fun Int.formatCompact(): String {
    val thousands = this.toDouble() / 1000
    return if (thousands >= 1.0) {
        "${decimalFormat.format(thousands)}K"
    } else {
        this.toString()
    }
}
