package com.example.nychighschools

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nychighschools.data.Highschool
import com.example.nychighschools.databinding.HighschoolItemBinding

class HighschoolsRecyclerAdapter(private var highschools: List<Highschool>): RecyclerView.Adapter<HighschoolsRecyclerAdapter.HighschoolViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighschoolViewHolder {
        val itemBinding = HighschoolItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HighschoolViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: HighschoolViewHolder, position: Int) {
        holder.bind(highschools[position])
    }

    override fun getItemCount(): Int {
        return highschools.size
    }

    // todo maybe will need this method in future.
    fun setHighschools(updatedHighschoolsList: List<Highschool>) {
        this.highschools = updatedHighschoolsList
        notifyDataSetChanged()
    }

    class HighschoolViewHolder(private val binding: HighschoolItemBinding): RecyclerView.ViewHolder(binding.root) {

        // todo add isExpanded save state and restore.
        fun bind(highschool: Highschool) {
            binding.schoolName.text = highschool.schoolName
            binding.satMath.text = NycHighschoolsApplication.appContext.getString(R.string.math_avg_score, highschool.satScoreMath)
            binding.satReading.text = NycHighschoolsApplication.appContext.getString(R.string.reading_avg_score, highschool.satScoreReading)
            binding.satWriting.text = NycHighschoolsApplication.appContext.getString(R.string.writing_avg_score, highschool.satScoreWriting)
            binding.showHideSat.setOnClickListener {
                highschool.isExpanded = !highschool.isExpanded
                binding.satContainer.visibility = if (highschool.isExpanded) View.VISIBLE else View.GONE
                binding.showHideSat.text = NycHighschoolsApplication.appContext.getString(
                    if (highschool.isExpanded) R.string.hide_sats else R.string.show_sats
                )
            }
        }
    }
}