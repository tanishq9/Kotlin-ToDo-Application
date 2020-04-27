package com.boss.login.onboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.boss.login.R
import com.boss.login.utils.PrefConstant
import com.boss.login.view.LoginActivity

class OnBoardingActivity : AppCompatActivity(), OnBoardingOneFragment.OnNextClick, OnBoardingTwoFragment.OnOptionClick {
    lateinit var viewPager: ViewPager
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        bindView()
        setUpSharedPreferences()
    }

    private fun setUpSharedPreferences() {
        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun bindView() {
        viewPager = findViewById(R.id.viewPager)
        val adapter = FragmentAdapter(supportFragmentManager)
        viewPager.adapter = adapter
    }

    // for first OnBoarding fragment
    override fun onClick() {
        viewPager.currentItem = 1
    }


    // for second OnBoarding fragment
    override fun onOptionBack() {
        viewPager.currentItem = 0
    }

    override fun onOptionDone() {
        // get editor, make changes(add/update/delete), then apply
        sharedPreferences.edit().putBoolean(PrefConstant.ON_BOARDED_SUCCESSFULLY, true).apply()
        startActivity(Intent(this@OnBoardingActivity, LoginActivity::class.java))
    }

}
// first time user
// splash -> onBoarding -> Login -> MyNotes

// 2nd time onwards
// splash -> Login (if not logged in) -> MyNotes
// splash -> MyNotes (if logged in)