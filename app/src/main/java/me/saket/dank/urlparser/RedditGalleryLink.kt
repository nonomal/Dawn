package me.saket.dank.urlparser

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import net.dean.jraw.models.Submission
import java.lang.IllegalStateException

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

        val ext = when (meta?.mime) {
          "image/jpeg" -> "jpg"
          "image/png" -> "png"
          "image/webp" -> "webp"
          else -> "jpg"
        }

        RedditGalleryImageLink("$it.$ext", lq)
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
    val lowQualityUrl: String?
): MediaLink(), Parcelable {
  override fun type(): Type = Type.SINGLE_IMAGE
  override fun unparsedUrl(): String = highQualityUrl()
  override fun cacheKey(): String = cacheKeyWithClassName(highQualityFilename)
  override fun lowQualityUrl(): String = lowQualityUrl ?: highQualityUrl()
  override fun highQualityUrl(): String = "https://i.redd.it/$highQualityFilename"
}
