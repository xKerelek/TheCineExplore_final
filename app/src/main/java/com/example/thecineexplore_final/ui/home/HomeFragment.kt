package com.example.thecineexplore_final.ui.home


import MoviesInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thecineexplore_final.Adapters.MoviesAdapter
import com.example.thecineexplore_final.R
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HomeFragment : Fragment() {
    private lateinit var topRatedMoviesAdapter: MoviesAdapter
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var upcomingMoviesAdapter: MoviesAdapter
    private lateinit var searchedMoviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Inicjalizacja adapterów dla RecyclerView
        topRatedMoviesAdapter = MoviesAdapter(requireContext())
        popularMoviesAdapter = MoviesAdapter(requireContext())
        upcomingMoviesAdapter = MoviesAdapter(requireContext())
        searchedMoviesAdapter = MoviesAdapter(requireContext())
        // Ustawienie adapterów dla RecyclerView
        val topRatedMoviesRecyclerView =
            rootView.findViewById<RecyclerView>(R.id.topRatedMoviesRecyclerView)
        val popularMoviesRecyclerView =
            rootView.findViewById<RecyclerView>(R.id.popularMoviesRecyclerView)
        val upcomingMoviesRecyclerView =
            rootView.findViewById<RecyclerView>(R.id.upcomingMoviesRecyclerView)
        val searchedMoviesRecyclerView =
            rootView.findViewById<RecyclerView>(R.id.searchedMoviesRecyclerView)

        topRatedMoviesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        topRatedMoviesRecyclerView.adapter = topRatedMoviesAdapter

        popularMoviesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        popularMoviesRecyclerView.adapter = popularMoviesAdapter

        upcomingMoviesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        upcomingMoviesRecyclerView.adapter = upcomingMoviesAdapter

        searchedMoviesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        searchedMoviesRecyclerView.adapter = searchedMoviesAdapter

        getTopRatedMovies()
        getPopularMovies()
        getUpcomingMovies()

        val sendMoviesNameButton = rootView.findViewById<ImageView>(R.id.sendMoviesName)
        sendMoviesNameButton.setOnClickListener {
            val enterMoviesName = rootView.findViewById<EditText>(R.id.enterMoviesName)
            val query = enterMoviesName.text.toString().trim()
            if (query.isNotEmpty()) {
                getSearchedMovies(query)
            } else {
                Toast.makeText(requireContext(), "Please enter a movie name", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return rootView
    }


    private fun getSearchedMovies(query: String) {
        val client = OkHttpClient()

        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.themoviedb.org")
            .addPathSegments("/3/search/movie")
            .addQueryParameter("include_adult", "false")
            .addQueryParameter("language", "en-US")
            .addQueryParameter("page", "1")
            .addQueryParameter("query", query)
            .addQueryParameter("api_key", "baadfc709c190c5c386a07bb6756cd83")
            .build()

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("HomeFragment", "Failed to fetch search movies: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                response.body?.close()

                if (responseData != null) {
                    val moviesInfo = Gson().fromJson(responseData, MoviesInfo::class.java)
                    requireActivity().runOnUiThread {
                        moviesInfo.results?.let { searchedMoviesAdapter.setMoviesList(it) }
                    }
                }
            }
        })
    }



    private fun getTopRatedMovies() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=1&api_key=baadfc709c190c5c386a07bb6756cd83")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "Failed to fetch top rated movies: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                response.body?.close()

                if (responseData != null) {
                    val moviesInfo = Gson().fromJson(responseData, MoviesInfo::class.java)
                    requireActivity().runOnUiThread {
                        moviesInfo.results?.let { topRatedMoviesAdapter.setMoviesList(it) }
                    }
                }
            }
        })
    }

    private fun getPopularMovies() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1&api_key=baadfc709c190c5c386a07bb6756cd83")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "Failed to fetch popular movies: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                response.body?.close()

                if (responseData != null) {
                    val moviesInfo = Gson().fromJson(responseData, MoviesInfo::class.java)
                    requireActivity().runOnUiThread {
                        moviesInfo.results?.let { popularMoviesAdapter.setMoviesList(it) }
                    }
                }
            }
        })
    }

    private fun getUpcomingMovies() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/upcoming?language=en-US&page=1&api_key=baadfc709c190c5c386a07bb6756cd83")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "Failed to fetch upcoming movies: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                response.body?.close()

                if (responseData != null) {
                    val moviesInfo = Gson().fromJson(responseData, MoviesInfo::class.java)
                    requireActivity().runOnUiThread {
                        moviesInfo.results?.let { upcomingMoviesAdapter.setMoviesList(it) }
                    }
                }
            }
        })
    }
}