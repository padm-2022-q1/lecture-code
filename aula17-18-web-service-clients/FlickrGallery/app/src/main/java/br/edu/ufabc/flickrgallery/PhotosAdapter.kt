package br.edu.ufabc.flickrgallery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.flickrgallery.databinding.PhotoItemBinding

class PhotosAdapter(
    private val photos: Photos,
    private val viewModel: MainViewModel,
    private val listFragment: ListFragment
    ) : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

        inner class PhotoViewHolder(itemBinding: PhotoItemBinding)
            : RecyclerView.ViewHolder(itemBinding.root) {
            val imageView = itemBinding.imageviewPhoto

            init {
                imageView.setOnClickListener {
                    ListFragmentDirections.showFullPhoto(
                        photos[bindingAdapterPosition].media.m).let {
                        listFragment.findNavController().navigate(it)
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PhotoViewHolder(PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val url = photos[holder.bindingAdapterPosition].media.m

        if(!URLUtil.isValidUrl(url)) throw Exception("URL is invalid")

        viewModel.downloadPhoto(url).observe(listFragment) { result ->
            when (result.status) {
                is MainViewModel.Status.Success -> {
                    holder.imageView.setImageBitmap(result.result)
                }
                is MainViewModel.Status.Error -> {
                    throw Exception("Failed to retrieve thumbnail: $url", result.status.e)
                }
            }
        }
    }

    override fun getItemCount(): Int = photos.size


}