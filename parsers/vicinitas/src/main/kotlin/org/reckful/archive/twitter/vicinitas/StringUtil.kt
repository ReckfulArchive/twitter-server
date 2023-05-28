package org.reckful.archive.twitter.vicinitas

internal fun checkNotEmpty(s: String): String = s.also { check(it.isNotEmpty()) }
internal fun takeIfNotBlank(s: String): String? = s.takeIf { it.isNotBlank() }
