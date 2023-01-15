package com.example.nychighschools

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.nychighschools.data.Highschool
import com.example.nychighschools.databinding.ActivityDetailsBinding
import com.google.gson.Gson

class DetailsActivity: AppCompatActivity() {

    companion object {

        private const val HIGHSCHOOL_KEY = "highschoolKey"

        fun getDetailsActivityIntent(context: Context, highschool: Highschool): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(HIGHSCHOOL_KEY, Gson().toJson(highschool))
            }
        }
    }

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val highschool = Gson().fromJson(intent.getStringExtra(HIGHSCHOOL_KEY), Highschool::class.java)
        initViews(highschool)
    }

    private fun initViews(highschool: Highschool) {
        binding.schoolName.text = highschool.schoolName
        binding.website.apply {
            visibility = if (!TextUtils.isEmpty(highschool.website)) View.VISIBLE else View.GONE
            movementMethod = LinkMovementMethod.getInstance()
            linksClickable = true
            autoLinkMask = Linkify.WEB_URLS
            text = getString(R.string.website, highschool.website)
        }
        if (!TextUtils.isEmpty(highschool.boro)) {
            binding.boro.visibility = View.VISIBLE
            binding.boro.text = highschool.boro
        } else {
            binding.boro.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(highschool.schoolEmail)) {
            binding.email.visibility = View.VISIBLE
            binding.email.text = getString(R.string.email, highschool.schoolEmail)
        } else {
            binding.email.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(highschool.faxNumber)) {
            binding.faxNumber.visibility = View.VISIBLE
            binding.faxNumber.text = getString(R.string.fax_number, highschool.faxNumber)
        } else {
            binding.faxNumber.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(highschool.bus)) {
            binding.bus.visibility = View.VISIBLE
            binding.bus.text = getString(R.string.bus, highschool.faxNumber)
        } else {
            binding.bus.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(highschool.address)) {
            binding.address.visibility = View.VISIBLE
            binding.address.text = getString(R.string.address, highschool.address)
        } else {
            binding.address.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(highschool.zip)) {
            binding.zip.visibility = View.VISIBLE
            binding.zip.text = getString(R.string.zip, highschool.zip)
        } else {
            binding.zip.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(highschool.totalStudents)) {
            binding.totalStudents.visibility = View.VISIBLE
            binding.totalStudents.text = getString(R.string.total_students, highschool.totalStudents)
        } else {
            binding.totalStudents.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(highschool.languageClasses)) {
            binding.languageClasses.visibility = View.VISIBLE
            binding.languageClasses.text = getString(R.string.language_classes, highschool.languageClasses)
        } else {
            binding.languageClasses.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(highschool.extracurricularActivities)) {
            binding.divider.visibility = View.VISIBLE
            binding.extraculicular.visibility = View.VISIBLE
            binding.extraculicular.text = getString(R.string.extracuricular, highschool.extracurricularActivities)
        } else {
            binding.divider.visibility = View.GONE
            binding.extraculicular.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(highschool.programHighlights)) {
            binding.programHighlights.visibility = View.VISIBLE
            binding.programHighlights.text = getString(R.string.highlights, highschool.programHighlights)
        } else {
            binding.programHighlights.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(highschool.overviewParagraph)) {
            binding.overview.visibility = View.VISIBLE
            binding.overview.text = getString(R.string.overview, highschool.overviewParagraph)
        } else {
            binding.overview.visibility = View.GONE
        }
    }
}
