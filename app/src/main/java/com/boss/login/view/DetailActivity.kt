package com.boss.login.view

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.boss.login.R
import com.boss.login.utils.AppConstant

class DetailActivity : AppCompatActivity() {
    lateinit var textViewTitle: TextView
    lateinit var textViewDescription: TextView
    val tag: String = "Activity:Detail"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(tag, "onCreate Called")
        setContentView(R.layout.activity_detail)
        bindView()
        getIntentData()
        supportActionBar?.title = textViewTitle.text.toString()
    }

    private fun getIntentData() {
        textViewTitle.text = intent.getStringExtra(AppConstant.TITLE)
        textViewDescription.text = intent.getStringExtra(AppConstant.DESCRIPTION)
    }

    private fun bindView() {
        textViewTitle = findViewById(R.id.textViewTitle)
        textViewDescription = findViewById(R.id.textViewDescription)
    }


}