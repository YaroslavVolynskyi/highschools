package com.example.nychighschools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nychighschools.databinding.ActivityHighschoolsBinding

class HighschoolsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHighschoolsBinding
    val highschoolViewModel: HighschoolsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHighschoolsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        highschoolViewModel.getHighschoolsLiveData().observe(this) { highschoolsList ->
            binding.highSchoolsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = HighschoolsRecyclerAdapter(highschoolsList)
            }
        }
        highschoolViewModel.fetchHighschoolsData()
    }
}
