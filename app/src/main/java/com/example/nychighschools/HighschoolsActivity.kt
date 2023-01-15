package com.example.nychighschools

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
        binding.forceRefreshButton.setOnClickListener { highschoolViewModel.forceRefresh() }
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
        highschoolViewModel.getErrorLiveData().observe(this) { showAlertDialog(this, it) }
        highschoolViewModel.fetchHighschoolsData()
        setSupportActionBar(binding.toolbar)
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

    private fun showAlertDialog(context: Context,
                                message: String?,
                                okClickListener: () -> Unit = {}) {
        AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton(R.string.ok_button) { dialog, which ->
                okClickListener.invoke()
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(true)
            .show()
    }
}
