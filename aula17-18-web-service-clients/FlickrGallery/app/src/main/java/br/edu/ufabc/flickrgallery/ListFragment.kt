package br.edu.ufabc.flickrgallery

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import br.edu.ufabc.flickrgallery.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.recyclerviewPhotos.layoutManager = GridLayoutManager(requireActivity(), 2)
        refresh(null)
        binding.swipeRefreshLayout.setOnRefreshListener {
            refresh(null)
        }
    }

    private fun refresh(tags: List<String>?) {
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.getRecentPhotos(tags).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                is MainViewModel.Status.Success -> {
                    val adapter = PhotosAdapter(result.result ?: emptyList(),
                        viewModel, this)
                    binding.recyclerviewPhotos.swapAdapter(adapter, false)
                }
                is MainViewModel.Status.Error -> {
                    Log.e("VIEW", "Failed to initialize recyclerview", result.status.e)
                    Snackbar.make(binding.root, "No data to display", Snackbar.LENGTH_LONG).show()
                }
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)

        menu.findItem(R.id.action_search).let { item ->
            (item.actionView as SearchView).apply {
                queryHint = "tag1, tag2, tag3"
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(tagString: String?): Boolean {
                        val tags = tagString?.split(",")?.map { it.trim() } ?: emptyList()
                        refresh(tags)
                        onActionViewCollapsed()
                        return true
                    }

                    override fun onQueryTextChange(p0: String?): Boolean = false

                })
            }
        }
    }
}