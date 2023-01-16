package com.example.nychighschools

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nychighschools.databinding.ActivityHighschoolsBinding

const val LIST_STATE_KEY = "listStateKey"

class HighschoolsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHighschoolsBinding
    private val highschoolViewModel: HighschoolsViewModel by viewModels { HighschoolsViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHighschoolsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.forceRefreshButton.setOnClickListener { highschoolViewModel.forceRefresh() }
        highschoolViewModel.getHighschoolsLiveData().observe(this) { highschoolsList ->
            binding.loadingIndicator.visibility = View.GONE
            binding.totalSchoolsTextView.text = getString(R.string.total_schools, highschoolsList.size)
            binding.highSchoolsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = HighschoolsRecyclerAdapter(highschoolsList) { highschool ->
                    // todo later registerForActivityResult if we would need some response from details activity.
                    startActivity(DetailsActivity.getDetailsActivityIntent(this@HighschoolsActivity, highschool))
                }
                if (adapter!!.itemCount > 0 && savedInstanceState?.getParcelable<Parcelable>(LIST_STATE_KEY) != null) {
                    layoutManager!!.onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE_KEY))
                }
            }
        }

        highschoolViewModel.getErrorLiveData().observe(this) { showAlertDialog(this, it) }

        // todo later some other kind of loading indicator can be implemented
        highschoolViewModel.getLoadingLiveData().observe(this) { shouldShowLoader ->
            binding.loadingIndicator.visibility = if (shouldShowLoader) View.VISIBLE else View.GONE
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
