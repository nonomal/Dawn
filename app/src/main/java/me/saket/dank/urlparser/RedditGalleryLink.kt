package me.saket.dank.urlparser

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import net.dean.jraw.models.Submission
import java.lang.IllegalStateException
import java.lang.UnsupportedOperationException

@JsonClass(generateAdapter = true)
@Parcelize
data class RedditGalleryLink(
    val galleryUrl: String,
    val imageList: List<RedditGalleryImageLink>
): MediaAlbumLink<RedditGalleryImageLink>(), Parcelable {

  companion object {
    @JvmStatic fun create(galleryUrl: String, submission: Submission): RedditGalleryLink {
      val images = submission.galleryData?.mediaIds?.map {
        val meta = submission.mediaMetadata?.get(it)
        val lq = meta?.previews?.last()?.url

        val (ext, mediaType) = when (meta?.mime) {
          "image/jpeg" -> Pair("jpg", Type.SINGLE_IMAGE)
          "image/png" -> Pair("png", Type.SINGLE_IMAGE)
          "image/webp" -> Pair("webp", Type.SINGLE_IMAGE)
          "image/gif" -> Pair("gif", Type.SINGLE_GIF)
          else -> throw UnsupportedOperationException("Unknown mime type: ${meta?.mime}")
        }

        RedditGalleryImageLink("$it.$ext", lq, mediaType)
      } ?: emptyList()

      if (images.isEmpty()) throw IllegalStateException("Attempting to create an empty gallery")
      return RedditGalleryLink(galleryUrl, images)
    }
  }

  override fun unparsedUrl(): String = galleryUrl
  override fun cacheKey(): String = cacheKeyWithClassName(galleryUrl)
  override fun coverImageUrl(): String = imageList[0].let { it.lowQualityUrl ?: it.highQualityUrl() }
  override fun images(): List<RedditGalleryImageLink> = imageList
}

@JsonClass(generateAdapter = true)
@Parcelize
data class RedditGalleryImageLink(
    val highQualityFilename: String,
    val lowQualityUrl: String?,
    val mediaType: Type
): MediaLink(), Parcelable {
  override fun type(): Type = mediaType
  override fun isGif(): Boolean = mediaType == Type.SINGLE_GIF
  override fun unparsedUrl(): String = highQualityUrl()
  override fun cacheKey(): String = cacheKeyWithClassName(highQualityFilename)
  override fun lowQualityUrl(): String = lowQualityUrl ?: highQualityUrl()
  override fun highQualityUrl(): String = "https://i.redd.it/$highQualityFilename"
}
