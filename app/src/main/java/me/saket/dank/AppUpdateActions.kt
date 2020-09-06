package me.saket.dank

import android.content.Context

class AppUpdateActions(private val appContext: Context) {
  companion object {
    const val UPDATE_FLAGS_PREFS = "update_flags"
    const val DATA_VERSION_FIELD = "data_version"

    const val CURRENT_DATA_VERSION = 2
    const val CLEAR_MEDIA_CACHE_AFTER = -1
    const val CLEAR_ROOM_CACHE_AFTER = 1
  }

  fun handleUpdate() {
    val flags = appContext.getSharedPreferences(UPDATE_FLAGS_PREFS, Context.MODE_PRIVATE)
    val prevData = flags.getInt(DATA_VERSION_FIELD, -1)
    if (prevData != CURRENT_DATA_VERSION && afterUpdate(prevData)) {
      val editor = flags.edit()
      editor.putInt(DATA_VERSION_FIELD, CURRENT_DATA_VERSION)
      editor.apply()
    }
  }

  private fun afterUpdate(prevData: Int): Boolean {
    val checkVersion: (Int) -> Boolean = {
      it > 0 && prevData <= it
    }

    if (checkVersion(CLEAR_MEDIA_CACHE_AFTER)) {
      appContext.cacheDir.deleteRecursively()
    }

    if (checkVersion(CLEAR_ROOM_CACHE_AFTER)) {
      appContext.deleteDatabase("Dank-room")
    }

    return true
  }
}
