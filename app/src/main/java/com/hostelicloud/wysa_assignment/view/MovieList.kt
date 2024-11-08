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
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.hostelicloud.wysa_assignment.R
import com.hostelicloud.wysa_assignment.databinding.FragmentMovieListBinding
import com.hostelicloud.wysa_assignment.model.Movie
import com.hostelicloud.wysa_assignment.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MoviePagingAdapter

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (isInternetAvailable()) {
                Toast.makeText(requireContext(), "Internet found", Toast.LENGTH_SHORT).show()
                loadData()
            } else {
                Toast.makeText(requireContext(), "No Internet Access (You are in Offline Mode)", Toast.LENGTH_SHORT).show()
            }
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

        setupRecyclerView()
        setupObservers()

        if (isInternetAvailable()) {
            binding.noDataText.visibility = View.GONE
            binding.recyclerViewMovies.visibility = View.VISIBLE
            loadData()
        } else {
            binding.noDataText.visibility = View.VISIBLE
            binding.recyclerViewMovies.visibility = View.GONE
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupCustomTabs()
    }

    private fun setupRecyclerView() {
        movieAdapter = MoviePagingAdapter { movie ->
            openMovieDetails(movie)
        }

        binding.recyclerViewMovies.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = movieAdapter
            setHasFixedSize(true)  // Ensures smoother scrolling by assuming a fixed layout size
            itemAnimator = null     // Disables change animations to prevent stutter on fast scroll
        }

        // Show progress bar based on loading state
        movieAdapter.addLoadStateListener { loadState ->
            binding.progressBar.visibility = if (loadState.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getMovies().collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
            }
        }

        viewModel.carouselImages.observe(viewLifecycleOwner) { images ->
            binding.viewPager.adapter = CarouselAdapter(images)
        }
    }

    private fun setupCustomTabs() {
        val tabs = listOf("Explore", "English", "French")

        tabs.forEach { title ->
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
                    0 -> viewModel.getMovies(language = null)
                    1 -> viewModel.getMovies(language = "en")
                    2 -> viewModel.getMovies(language = "fr")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getMovies().collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requireContext().registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(networkReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
