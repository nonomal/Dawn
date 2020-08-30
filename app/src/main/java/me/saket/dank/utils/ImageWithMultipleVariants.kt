package me.saket.dank.utils

import android.text.Html
import me.saket.dank.urlparser.UrlParser
import net.dean.jraw.models.MediaMetadataItem
import net.dean.jraw.models.MediaMetadataPreview
import net.dean.jraw.models.SubmissionPreview
import java.util.*
import kotlin.math.abs

data class ImageVariant(
    val width: Int,
    val height: Int,
    private val rawUrl: String,
    private val urlIsHtmlEncoded: Boolean
) {
  /**
   * Returned URL is never escaped
   */
  val url: String
    get() = if (urlIsHtmlEncoded) Html.fromHtml(rawUrl).toString() else rawUrl
}

class ImageWithMultipleVariants(
    val source: ImageVariant?,
    val variants: List<ImageVariant>
) {

  val isNonEmpty: Boolean
    get() = source != null

  fun orElse(other: () -> ImageWithMultipleVariants): ImageWithMultipleVariants =
      if (this.isNonEmpty) this else other()

  /**
   * Find an image provided by Reddit that is the closest to <var>preferredWidth</var>.
   * Gives preference to higher-res thumbnails if multiple images have the same distance from the preferred width.
   *
   * @param minWidth Minimum preview width.
   * Specify -1 to find any preview. minWidth is ignored if it larger than preferredWidth
   */
  fun findNearestFor(preferredWidth: Int, minWidth: Int): ImageVariant? {
    if (source == null) return null

    val minWidthChecked = if (minWidth > preferredWidth) -1 else minWidth

    var closestImage: ImageVariant = source
    var closestDifference = preferredWidth - source.width

    for (variation in variants) {
      val differenceAbs = abs(preferredWidth - variation.width)
      if (differenceAbs < abs(closestDifference)
        // If another image is found with the same difference, choose the higher-res image.
        || differenceAbs == closestDifference && variation.width > closestImage.width
      ) {
        closestDifference = preferredWidth - variation.width
        closestImage = variation
      }
    }

    return if (closestImage.width < minWidthChecked) null else closestImage
  }

  @Suppress("DEPRECATION")
  fun findNearestUrlFor(preferredWidth: Int, minWidth: Int): String? =
    findNearestFor(preferredWidth, minWidth)?.url

  fun findNearestUrlFor(preferredWidth: Int): String {
    return findNearestUrlFor(preferredWidth, -1) ?:
      throw NoSuchElementException("No reddit supplied images present")
  }

  fun findNearestUrlFor(preferredWidth: Int, minWidth: Int, defaultValue: String): String {
    if (UrlParser.isGifUrl(defaultValue)) {
      throw AssertionError("Optimizing GIFs is an error: $defaultValue")
    }

    return findNearestUrlFor(preferredWidth, minWidth) ?: defaultValue
  }

  fun findNearestUrlFor(preferredWidth: Int, defaultValue: String): String {
    return findNearestUrlFor(preferredWidth, -1, defaultValue)
  }

  companion object {

    const val DEFAULT_VIEWER_MIN_WIDTH = 1200

    fun of(mediaMetadata: MediaMetadataItem?): ImageWithMultipleVariants {
      val mapF: (MediaMetadataPreview) -> ImageVariant =
          { ImageVariant(it.width, it.height, it.url, urlIsHtmlEncoded = true) }
      return ImageWithMultipleVariants(
          mediaMetadata?.full?.let(mapF),
          mediaMetadata?.previews?.map(mapF) ?: emptyList()
      )
    }

    fun of(redditSuppliedImages: SubmissionPreview?): ImageWithMultipleVariants {
      val mapF: (SubmissionPreview.Variation) -> ImageVariant =
          { ImageVariant(it.width, it.height, it.url, urlIsHtmlEncoded = true) }
      val img = redditSuppliedImages?.images?.getOrNull(0)
      return ImageWithMultipleVariants(img?.source?.let(mapF), img?.resolutions?.map(mapF) ?: emptyList())
    }

    fun of(redditSuppliedImages: Optional<SubmissionPreview>): ImageWithMultipleVariants =
      this.of(redditSuppliedImages.value())
  }
}