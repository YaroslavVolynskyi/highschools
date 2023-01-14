package com.example.nychighschools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nychighschools.databinding.ActivityHighschoolsBinding

const val LIST_STATE_KEY = "listStateKey"

class HighschoolsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHighschoolsBinding
    val highschoolViewModel: HighschoolsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHighschoolsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        highschoolViewModel.getHighschoolsLiveData().observe(this) { highschoolsList ->
            binding.totalSchoolsTextView.text = getString(R.string.total_schools, highschoolsList.size)
            binding.highSchoolsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = HighschoolsRecyclerAdapter(highschoolsList)
                if (adapter!!.itemCount > 0 && savedInstanceState?.getParcelable<Parcelable>(LIST_STATE_KEY) != null) {
                    layoutManager!!.onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE_KEY))
                }
            }
        }
        highschoolViewModel.fetchHighschoolsData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (binding.highSchoolsRecyclerView.layoutManager != null) {
            outState.putParcelable(
                LIST_STATE_KEY,
                binding.highSchoolsRecyclerView.layoutManager!!.onSaveInstanceState()
            )
        }
        super.onSaveInstanceState(outState)
    }
}
