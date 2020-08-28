package me.saket.dank.urlparser

/**
 * An image, GIF or a video. See implementations.
 */
abstract class MediaLink : Link() {
  abstract fun highQualityUrl(): String?
  abstract fun lowQualityUrl(): String?
  abstract fun cacheKey(): String?

  override fun isGif(): Boolean {
    return UrlParser.isGifUrl(unparsedUrl())
  }

  fun cacheKeyWithClassName(key: String): String {
    val name = javaClass.simpleName
    val nameWithoutAutoValue = if (name.startsWith("AutoValue_")) name.substring("AutoValue_".length) else name
    return nameWithoutAutoValue + "_" + key
  }
}
