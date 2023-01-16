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
import com.example.nychighschools.utils.init
import com.google.gson.Gson

// todo Given more time I would definitely create more sophisticated design for this activity :)
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
        binding.backIcon.setOnClickListener { finish() }
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

        binding.divider.visibility = if ((!TextUtils.isEmpty(highschool.extracurricularActivities))
            || !TextUtils.isEmpty(highschool.overviewParagraph)) View.VISIBLE else View.GONE
        binding.boro.init(highschool.boro)
        binding.email.init(getString(R.string.email, highschool.schoolEmail))
        binding.faxNumber.init(getString(R.string.fax_number, highschool.faxNumber))
        binding.bus.init(getString(R.string.bus, highschool.faxNumber))
        binding.address.init(getString(R.string.address, highschool.address))
        binding.zip.init(getString(R.string.zip, highschool.zip))
        binding.totalStudents.init(getString(R.string.total_students, highschool.totalStudents))
        binding.languageClasses.init(getString(R.string.language_classes, highschool.languageClasses))
        binding.extraculicular.init(getString(R.string.extracuricular, highschool.extracurricularActivities))
        binding.programHighlights.init(getString(R.string.highlights, highschool.programHighlights))
        binding.overview.init(getString(R.string.overview, highschool.overviewParagraph))
    }
}
