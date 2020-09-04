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
      val images = submission.galleryData?.items?.map {
        val id = it.mediaId
        val meta = submission.mediaMetadata?.get(id)
        val lq = meta?.previews?.last()?.imgUrl

        val (hqUrl, mediaType) = if (meta != null && meta.mime == "image/gif") {
          meta.full.mp4Url
              ?.let { u -> Pair(u, Type.SINGLE_VIDEO) }
              ?: Pair("https://i.redd.it/$id.gif", Type.SINGLE_GIF)
        } else {
          val ext = when (meta?.mime) {
            "image/jpeg", "image/jpg" -> "jpg"
            "image/png" -> "png"
            "image/webp" -> "webp"
            else -> throw UnsupportedOperationException("Unknown mime type: ${meta?.mime}")
          }
          Pair("https://i.redd.it/$id.$ext", Type.SINGLE_IMAGE)
        }

        RedditGalleryImageLink(hqUrl, lq, mediaType, it.caption)
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
    val highQualityUrl: String,
    val lowQualityUrl: String?,
    val mediaType: Type,
    val title: String?
): MediaLink(), Parcelable {
  override fun type(): Type = mediaType
  override fun isGif(): Boolean = mediaType == Type.SINGLE_GIF
  override fun unparsedUrl(): String = highQualityUrl()
  override fun cacheKey(): String = cacheKeyWithClassName(highQualityUrl)
  override fun lowQualityUrl(): String = lowQualityUrl ?: highQualityUrl
  override fun highQualityUrl(): String = highQualityUrl
  override fun title(): String? = title
}
