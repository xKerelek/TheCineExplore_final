package com.example.thecineexplore_final.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.content.Context
import android.content.Intent
import com.example.thecineexplore_final.Domain.MovieResult
import com.example.thecineexplore_final.R
import com.example.thecineexplore_final.activities.DetailActivity

class MoviesAdapter(private val context: Context) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private var moviesList: List<MovieResult> = emptyList()

    fun setMoviesList(moviesList: List<MovieResult>) {
        this.moviesList = moviesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = moviesList[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener {

            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("MOVIE", movie)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }




    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.movieTitleText)
        private val pictureImageView: ImageView = itemView.findViewById(R.id.pictureOfMovies)

        fun bind(movie: MovieResult) {
            titleTextView.text = movie.title
             Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background))
                .into(pictureImageView)
        }
    }
}