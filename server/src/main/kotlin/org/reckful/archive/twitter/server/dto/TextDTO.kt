package org.reckful.archive.twitter.server.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Text",
    description = "Represents a text in a number of ways."
)
class TextDTO(

    @Schema(description = "Text in plain view. Contains links, mentions, hashtags as is.")
    val plain: String,

    @Schema(
        description = "Tokenized text. Can be used to avoid parsing the text on client side, " +
                "or to extract only specific bits of information."
    )
    val tokenized: List<TextTokenDTO>,

    @Schema(description = "List of URLs that have been found in this text.")
    val knownUrls: List<String>,

    @Schema(description = "List of profile handles that have been mentioned in this text.")
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
@Schema(
    name = "BaseTextToken",
    description = "Base information found in all tokens",
    subTypes = [PlainTextTokenDTO::class, LinkTextTokenDTO::class]
)
abstract class TextTokenDTO(
    @Schema(description = "Text of this token.")
    val text: String
)

@Schema(name = "PlainTextToken", description = "Plain text that contains no embedded data.")
class PlainTextTokenDTO(text: String) : TextTokenDTO(text)

@Schema(
    name = "LinkTextToken",
    description = "A part of text that represents a link, either to an external resource or to another profile."
)
class LinkTextTokenDTO(
    text: String,

    @Schema(description = "URL that this link leads to.")
    val url: String
) : TextTokenDTO(text)
