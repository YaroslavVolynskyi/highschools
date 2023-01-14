package com.example.nychighschools

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nychighschools.data.Highschool
import com.example.nychighschools.databinding.HighschoolBinding

class HighschoolsRecyclerAdapter(private var highschools: List<Highschool>): RecyclerView.Adapter<HighschoolsRecyclerAdapter.HighschoolViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighschoolViewHolder {
        val itemBinding = HighschoolBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HighschoolViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: HighschoolViewHolder, position: Int) {
        holder.bind(highschools[position])
    }

    override fun getItemCount(): Int {
        return highschools.size
    }

    fun setHighschools(updatedHighschoolsList: List<Highschool>) {
        this.highschools = updatedHighschoolsList
        notifyDataSetChanged()
    }

    class HighschoolViewHolder(private val binding: HighschoolBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(highschool: Highschool) {
            binding.schoolName.text = highschool.schoolName
        }
    }
}