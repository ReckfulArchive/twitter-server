package org.reckful.archive.twitter.server.model

enum class SortOrder {
    ASC, DESC;

    companion object {
        // TODO change to EnumEntries
        fun fromString(s: String): SortOrder? = values().firstOrNull { s.uppercase() == it.name }
    }
}
