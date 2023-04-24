package org.reckful.archive.twitter.generator.parts

import kotlinx.html.*
import org.reckful.archive.twitter.model.GifMedia
import org.reckful.archive.twitter.model.Media
import org.reckful.archive.twitter.model.PhotoMedia
import org.reckful.archive.twitter.model.VideoMedia

class MediaHtmlGenerator {

    fun FlowContent.generateMediaContent(media: List<Media>) {
        if (media.isEmpty()) {
            return
        }

        when {
            media.containsPhotosOnly() -> photoContent(media)
            media.containsSingleVideoOnly() -> videoContent(media.single())
            media.containsSingleGifOnly() -> gifContent(media.single())
            else -> error("Expected media to be either a single video, a single gif or 1-4 photos only")
        }
    }

    private fun FlowContent.photoContent(media: List<Media>) {
        media.forEach { pic ->
            div(classes = "tweet-pic") {
                img(src = pic.url, alt = "Picture in the Tweet") {
                    addLazyLoadingAttribute()
                }
            }
        }
    }

    private fun FlowContent.videoContent(media: Media) {
        div(classes = "tweet-pic") {
            video {
                controls = true
                src = media.url

                if (media.isMp4()) {
                    attributes["type"] = "video/mp4"
                }
                addLazyLoadingAttribute()
            }
        }
    }

    private fun FlowContent.gifContent(media: Media) {
        div(classes = "tweet-pic") {
            img(src = media.url, alt = "Gif in the Tweet") {
                addLazyLoadingAttribute()
            }
        }
    }

    private fun Tag.addLazyLoadingAttribute() {
        attributes["loading"] = "lazy"
    }

    private fun List<Media>.containsSingleVideoOnly() = this.size == 1 && this[0] is VideoMedia
    private fun List<Media>.containsSingleGifOnly() = this.size == 1 && this[0] is GifMedia
    private fun List<Media>.containsPhotosOnly() = this.isNotEmpty() && this.all { it is PhotoMedia }

    private fun Media.isMp4(): Boolean = this.url.contains(".mp4")
}
