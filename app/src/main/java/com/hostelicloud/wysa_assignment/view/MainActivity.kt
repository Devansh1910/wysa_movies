package com.hostelicloud.wysa_assignment.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hostelicloud.wysa_assignment.R
import com.hostelicloud.wysa_assignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load the default fragment (MovieListFragment)
        loadFragment(MovieListFragment())

        // Set up the BottomNavigationView
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_movies -> {
                    loadFragment(MovieListFragment())
                    true
                }
                R.id.navigation_download -> {
                    loadFragment(DownloadFragment())
                    true
                }
                else -> false
            }
        }
    }

    // Helper function to load fragments
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
