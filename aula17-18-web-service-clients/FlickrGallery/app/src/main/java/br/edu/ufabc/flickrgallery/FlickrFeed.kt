package br.edu.ufabc.flickrgallery

import android.net.Uri
import com.beust.klaxon.Klaxon
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class FlickrFeed {

    fun publicPhotos(tags: List<String>?): List<FlickrResult.Item> = try {
        val baseUrl = "https://www.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1"
        val uri = Uri.parse(baseUrl).buildUpon().apply {
            tags?.joinToString(",")?.let {
                appendQueryParameter("tags", it)
            }
        }.build()
        val connection = URL(uri.toString()).openConnection() as HttpsURLConnection

        connection.requestMethod = "GET"
        connection.connect()

        when (connection.responseCode) {
            HttpsURLConnection.HTTP_OK -> connection.inputStream.use { inputStream ->
                Klaxon().parse<FlickrResult>(inputStream)?.items
                    ?: throw Exception("Failed to parse JSON response")
            }
            else -> throw Exception("Invalid response code")
        }
    } catch (e: Exception) {
        throw Exception("Failed to fetch recent photos", e)
    }
}