package com.example.thecineexplore_final.ui.anime


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.thecineexplore_final.databinding.FragmentAnimeBinding
import com.example.thecineexplore_final.ui.anime.controller.api.AnimeAPIController
import com.zlatamigas.animind.controller.recyclerview.AnimeRVAdapter
import com.zlatamigas.animind.controller.recyclerview.AnimeRVModal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class AnimeFragment : Fragment() {
    private var _binding: FragmentAnimeBinding? = null
    private val binding get() = _binding!!
    private var page = 0

    private lateinit var idRVAnimeListFound: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageView
    private lateinit var animeRVModalArrayList: ArrayList<AnimeRVModal>
    private lateinit var animeRVAdapter: AnimeRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        idRVAnimeListFound = binding.idRVAnimeListFound
        searchEditText = binding.idTIESearch
        searchButton = binding.idIVSearch

        animeRVModalArrayList = ArrayList()
        animeRVAdapter = AnimeRVAdapter(requireActivity(), animeRVModalArrayList!!)
        idRVAnimeListFound.adapter = animeRVAdapter

        searchButton.setOnClickListener {
            performSearch()
        }
        displayResultsForQuery("Naruto")

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun performSearch() {
        val query = searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            displayResultsForQuery(query)
        } else {
            Toast.makeText(requireContext(), "Please enter a search query", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayResultsForQuery(query: String) {
        GlobalScope.launch {
            val controller = AnimeAPIController(requireContext())
            val animes = controller.getAnimes(
                searchString = query,
                sortBy = null,
                filterBy = null,
                filters = null,
                page = page
            )
            withContext(Dispatchers.Main) {
                animeRVModalArrayList.clear()
                if (animes != null) {
                    animeRVModalArrayList.addAll(animes.map { anime ->
                        AnimeRVModal(
                            anime.id,
                            anime.title,
                            anime.rating.toString(),
                            anime.episodeCount.toString(),
                            anime.posterImage
                        )
                    })
                    animeRVAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}
