package com.syncrown.mviarchitechturepractice

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.mviarchitechturepractice.api.AnimalService
import com.syncrown.mviarchitechturepractice.databinding.ActivityMainBinding
import com.syncrown.mviarchitechturepractice.view.AnimalListAdapter
import com.syncrown.mviarchitechturepractice.view.MainIntent
import com.syncrown.mviarchitechturepractice.view.MainState
import com.syncrown.mviarchitechturepractice.view.MainViewModel
import com.syncrown.mviarchitechturepractice.view.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var mainViewModel: MainViewModel
    private var adapter = AnimalListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        setupObservables()

    }

    private fun setupUI() {
        mainViewModel = ViewModelProvider(this, ViewModelFactory(AnimalService.api))[MainViewModel::class.java]
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.run {
            addItemDecoration(
                DividerItemDecoration(
                    binding.recyclerView.context,
                    (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
        }
        binding.recyclerView.adapter = adapter
        binding.buttonFetchAnimals.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.FetchAnimals)
            }
        }
    }

    private fun setupObservables() {
        lifecycleScope.launch {
            mainViewModel.state.collect { collector->
                when (collector) {
                    is MainState.Idle -> {

                    }
                    is MainState.Loading -> {
                        binding.buttonFetchAnimals.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MainState.Animals -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonFetchAnimals.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        collector.animals.let {
                            adapter.newAnimals(it)
                        }
                    }
                    is MainState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonFetchAnimals.visibility = View.GONE
                        Toast.makeText(this@MainActivity, collector.error, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
}