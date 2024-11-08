package com.hostelicloud.wysa_assignment.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hostelicloud.wysa_assignment.R

class MovieDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        // Get data from intent
        val title = intent.getStringExtra("title")
        val overview = intent.getStringExtra("overview")
        val posterPath = intent.getStringExtra("poster_path")

        // Initialize views
        val titleView: TextView = findViewById(R.id.movieTitle)
        val movietitleView: TextView = findViewById(R.id.movieTitle1)
        val overviewView: TextView = findViewById(R.id.movieOverview)
        val posterImageView: ImageView = findViewById(R.id.moviePoster)
        val likeButton: TextView = findViewById(R.id.like)
        val rateButton: TextView = findViewById(R.id.ratenow)

        // Set data to views
        titleView.text = title
        movietitleView.text = title
        overviewView.text = overview
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$posterPath")
            .into(posterImageView)

        // Set click listeners for "Like" and "Rate Movie" buttons
        likeButton.setOnClickListener {
            Toast.makeText(this, "Login to the account for further interactions", Toast.LENGTH_SHORT).show()
        }

        rateButton.setOnClickListener {
            showRateAlertDialog()
        }
    }

    private fun showRateAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Opening Wysa Website")
        builder.setMessage("You are about to be redirected to the Wysa website.")
        builder.setPositiveButton("Continue") { dialog, _ ->
            dialog.dismiss()
            openWysaWebsite()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun openWysaWebsite() {
        val rateIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.wysa.com/"))
        startActivity(rateIntent)
    }
}
