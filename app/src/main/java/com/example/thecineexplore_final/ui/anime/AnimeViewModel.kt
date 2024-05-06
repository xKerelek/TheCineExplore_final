package com.example.thecineexplore_final.ui.anime

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thecineexplore_final.R
import com.example.thecineexplore_final.ui.anime.controller.api.AnimeAPIController
import com.example.thecineexplore_final.ui.anime.controller.db.DBController
import com.example.thecineexplore_final.ui.anime.controller.db.DBHelper
import com.example.thecineexplore_final.ui.anime.controller.notification.AnimeNotification
import com.example.thecineexplore_final.ui.anime.controller.notification.bigImage
import com.example.thecineexplore_final.ui.anime.controller.notification.bigText
import com.example.thecineexplore_final.ui.anime.controller.notification.channelID
import com.example.thecineexplore_final.ui.anime.controller.notification.notificationID
import com.example.thecineexplore_final.ui.anime.controller.notification.titleExtra
import com.squareup.picasso.Picasso
import com.zlatamigas.animind.model.Anime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AnimeViewModel : AppCompatActivity() {
    private lateinit var idIVAddFavourite: ImageView
    private lateinit var idIVAnimeCover: ImageView

    private lateinit var idTVAnimeTitle: TextView
    private lateinit var idTVAnimeRating: TextView
    private lateinit var idTVAnimeYears: TextView
    private lateinit var idTVAnimeEpisodes: TextView
    private lateinit var idTVAnimeAge: TextView
    private lateinit var idTVAnimeGenres: TextView
    private lateinit var idTVAnimeDescription: TextView

    private lateinit var idPBLoadAnimePage: ProgressBar
    private lateinit var idSVAnimeData: ScrollView

    private lateinit var dbController: DBController

    private var isFavourite = false

    private var idAnime: Int = -1
    private var anime: Anime? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime)
        createNotificationChannel()


        dbController = DBController(DBHelper(this))

        idAnime = intent.extras?.getInt("idAnime") ?: -1

        idIVAddFavourite = findViewById(R.id.idIVAddFavourite)
        idIVAnimeCover = findViewById(R.id.idIVAnimeCover)

        idPBLoadAnimePage = findViewById(R.id.idPBLoadAnimePage)
        idSVAnimeData = findViewById(R.id.idSVAnimeData)

        idTVAnimeTitle = findViewById(R.id.idTVAnimeTitle)
        idTVAnimeRating = findViewById(R.id.idTVAnimeRating)
        idTVAnimeYears = findViewById(R.id.idTVAnimeYears)
        idTVAnimeEpisodes = findViewById(R.id.idTVAnimeEpisodes)
        idTVAnimeAge = findViewById(R.id.idTVAnimeAge)
        idTVAnimeGenres = findViewById(R.id.idTVAnimeGenres)
        idTVAnimeDescription = findViewById(R.id.idTVAnimeDescription)

        isFavourite = dbController.isFavourite(idAnime)

        idIVAddFavourite.setImageResource(
            when {
                isFavourite -> R.drawable.ic_star_selected
                else -> R.drawable.ic_star_not_selected
            }
        )

        idIVAddFavourite.setOnClickListener {
            isFavourite = !isFavourite

            idIVAddFavourite.setImageResource(
                when {
                    isFavourite -> R.drawable.ic_star_selected
                    else -> R.drawable.ic_star_not_selected
                }
            )

            when {
                isFavourite -> dbController.addFavourite(idAnime, anime!!.title)
                else -> dbController.deleteFavourite(idAnime)
            }
        }

        val cm =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if (!isConnected) {
            Toast.makeText(applicationContext, "Require Internet Connection", Toast.LENGTH_SHORT)
                .show()
            finish()
            return
        }

        val controller = AnimeAPIController(this)
        GlobalScope.launch {

            anime = controller.getAnime(idAnime)

            withContext(Dispatchers.Main) {
                idTVAnimeTitle.text = checkNullString(anime!!.title)
                idTVAnimeRating.text = "${checkNullString(anime!!.rating)}/100"

                val output = SimpleDateFormat("MM.yyyy")

                var startDate = "Unknown"
                var endDate = "Still running"
                if (anime!!.startDate != null) {
                    startDate = output.format(anime!!.startDate)
                }
                if (anime!!.endDate != null) {
                    endDate = output.format(anime!!.endDate)
                }

                idTVAnimeYears.text = "$startDate - $endDate"
                idTVAnimeEpisodes.text =
                    "Episodes: ${checkNullString(anime!!.episodeCount)} (${checkNullString(anime!!.episodeLength)} min)"
                idTVAnimeAge.text = "${checkNullString(anime!!.ageRating)}"
                idTVAnimeGenres.text = "Genres: "

                anime!!.genres.forEach { idTVAnimeGenres.append("${checkNullString(it)} ") }

                idTVAnimeDescription.text = checkNullString(anime!!.synopsis)
                Picasso.get().load(anime!!.posterImage).fit().into(idIVAnimeCover)

                idPBLoadAnimePage.visibility = View.GONE
                idSVAnimeData.visibility = View.VISIBLE
            }
        }
    }

    private fun checkNullString(str: Any?): String = when (str) {
        null -> "--"
        else -> str.toString()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            channelID,
            "Notification Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "A Description of the Channel"
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}