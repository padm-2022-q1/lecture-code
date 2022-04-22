package br.edu.ufabc.flickrgallery

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = Repository()

    sealed class Status {
        class Error(val e: Exception) : Status()
        object Success : Status()
    }

    data class PhotosResult(
        val result: Photos?,
        val status: Status
    )

    data class BitmapResult(
        val result: Bitmap?,
        val status: Status
    )

    fun getRecentPhotos(tags: List<String>?) = liveData {
        try {
            emit(PhotosResult(repository.getRecentPhotos(tags), Status.Success))
        } catch (e: Exception) {
            emit(PhotosResult(null, Status.Error(Exception("Failed to load data", e))))
        }
    }

    fun downloadPhoto(url: String) = liveData {
        try {
            emit(BitmapResult(repository.getBitmapFromUrl(url), Status.Success))
        } catch (e: Exception) {
            emit(BitmapResult(null,
                Status.Error(Exception("Failed to download photo", e))))
        }
    }
}