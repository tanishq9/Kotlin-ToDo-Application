package com.boss.login.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.boss.login.R

class OnBoardingActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        bindView()
    }

    fun bindView() {
        viewPager = findViewById(R.id.viewPager)
        val adapter = FragmentAdapter(supportFragmentManager)
        viewPager.adapter = adapter
    }

}
