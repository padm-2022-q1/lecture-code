package br.edu.ufabc.flickrgallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class Repository {
    private val feed = FlickrFeed()

    suspend fun getRecentPhotos(tags: List<String>? = null): List<FlickrResult.Item> = withContext(Dispatchers.IO) {
        feed.publicPhotos(tags)
    }

    suspend fun getBitmapFromUrl(url: String): Bitmap = withContext(Dispatchers.IO) {
        getMedia(url)
    }

    fun getMedia(url: String): Bitmap = try {
        Log.d("MODEL", "Downloading $url")
        BitmapFactory.decodeStream(URL(url).openStream())
    } catch (e: Exception) {
        throw Exception("Failed to download thumbnail: $url", e)
    }

}