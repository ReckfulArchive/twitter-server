package org.reckful.archive.twitter.server.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

class TextDTO(
    val plain: String,
    val tokenized: List<TextTokenDTO>,
    val knownUrls: List<String>,
    val knownHandles: List<String>,
) {
    override fun toString(): String {
        return "TextDTO(plain='$plain')"
    }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = PlainTextTokenDTO::class, name = "text"),
    JsonSubTypes.Type(value = LinkTextTokenDTO::class, name = "link"),
)
abstract class TextTokenDTO(
    val text: String
)

class PlainTextTokenDTO(text: String) : TextTokenDTO(text)
class LinkTextTokenDTO(text: String, val url: String) : TextTokenDTO(text)
