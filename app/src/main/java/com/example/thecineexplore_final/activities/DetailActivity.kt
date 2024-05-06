package com.example.thecineexplore_final.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.thecineexplore_final.Domain.MovieResult
import com.example.thecineexplore_final.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_activity)

        val movie = intent.getSerializableExtra("MOVIE") as MovieResult

        val movieTitleTextView = findViewById<TextView>(R.id.movieTitleDetail)
        val scoreTextView = findViewById<TextView>(R.id.score)
        val popularityTextView = findViewById<TextView>(R.id.popularity)
        val releaseTextView = findViewById<TextView>(R.id.release)
        val synopsisTextView = findViewById<TextView>(R.id.synopsis)
        val pictureImageView = findViewById<ImageView>(R.id.moviePicture)

        movieTitleTextView.text = movie.title
        scoreTextView.text = "Score: ${movie.voteAverage}"
        popularityTextView.text = "Popularity: ${movie.popularity}"
        releaseTextView.text = "Release Date: ${movie.releaseDate}"
        synopsisTextView.text = movie.overview

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background))
            .into(pictureImageView)
    }
}