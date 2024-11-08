package com.hostelicloud.wysa_assignment.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.hostelicloud.wysa_assignment.R
import com.hostelicloud.wysa_assignment.databinding.FragmentMovieListBinding
import com.hostelicloud.wysa_assignment.model.Movie
import com.hostelicloud.wysa_assignment.viewmodel.MovieViewModel

class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieViewModel by viewModels()

    private var isOffline = true // Track the current internet status

    // BroadcastReceiver to listen for connectivity changes
    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val hasInternet = isInternetAvailable()
            if (hasInternet && isOffline) {
                // Show "Internet found" toast only when coming back online
                Toast.makeText(requireContext(), "Internet found", Toast.LENGTH_SHORT).show()
                loadData() // Load data when internet is found
            }
            // Update the offline status based on current connectivity
            isOffline = !hasInternet
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initial internet check and setup UI
        if (isInternetAvailable()) {
            Toast.makeText(requireContext(), "Live", Toast.LENGTH_SHORT).show()
            binding.noDataText.visibility = View.GONE
            binding.recyclerViewMovies.visibility = View.VISIBLE
            loadData()
            isOffline = false // Set the flag to false if internet is initially available
        } else {
            Toast.makeText(requireContext(), "No Internet Access (You are in Offline Mode)", Toast.LENGTH_SHORT).show()
            binding.noDataText.visibility = View.VISIBLE
            binding.recyclerViewMovies.visibility = View.GONE
            isOffline = true // Set the flag to true if internet is initially unavailable
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupCustomTabs()

        viewModel.carouselImages.observe(viewLifecycleOwner) { images ->
            binding.viewPager.adapter = CarouselAdapter(images)
        }

        binding.recyclerViewMovies.layoutManager = GridLayoutManager(requireContext(), 3)

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (movies.isNullOrEmpty()) {
                binding.noDataText.visibility = View.VISIBLE
                binding.recyclerViewMovies.visibility = View.GONE
            } else {
                binding.noDataText.visibility = View.GONE
                binding.recyclerViewMovies.visibility = View.VISIBLE
                binding.recyclerViewMovies.adapter = MovieAdapter(movies) { movie ->
                    // Handle movie click to launch MovieDetailsActivity
                    openMovieDetails(movie)
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.errorHandled()
            }
        }
    }

    private fun setupCustomTabs() {
        val tabs = listOf("Explore", "English", "French")

        tabs.forEachIndexed { index, title ->
            val tab = binding.tabswillcome.newTab()
            val tabView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null)

            val text = tabView.findViewById<TextView>(R.id.tab_text)
            text.text = title

            tab.customView = tabView
            binding.tabswillcome.addTab(tab)
        }

        binding.tabswillcome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.fetchMovies(language = null)
                    1 -> viewModel.fetchMovies(language = "en")
                    2 -> viewModel.fetchMovies(language = "fr")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewModel.fetchMovies(language = null)
    }

    private fun openMovieDetails(movie: Movie) {
        val intent = Intent(requireContext(), MovieDetailsActivity::class.java).apply {
            putExtra("title", movie.title)
            putExtra("overview", movie.overview)
            putExtra("poster_path", movie.poster_path)
        }
        startActivity(intent)
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun loadData() {
        binding.noDataText.visibility = View.GONE
        binding.recyclerViewMovies.visibility = View.VISIBLE
        viewModel.fetchMovies(language = null) // Fetch movies from the ViewModel
    }

    override fun onStart() {
        super.onStart()
        // Register network receiver when fragment is visible
        requireContext().registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        // Unregister network receiver when fragment is not visible
        requireContext().unregisterReceiver(networkReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
