package com.boss.login.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.boss.login.R
import com.boss.login.onboarding.OnBoardingActivity
import com.boss.login.utils.PrefConstant
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class SplashActivity : AppCompatActivity() {
    // var sharedPreferences: SharedPreferences? = null
    lateinit var sharedPreferences: SharedPreferences
    val TAG: String = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setUpSharedPreferences()
        checkLoginStatus()
        getFCMToken()
    }

    private fun getFCMToken() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
                    // Log.d(TAG, token)
                    // Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
                })

    }

    private fun setUpSharedPreferences() {
        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private fun checkLoginStatus() {
        val isLoggedIn = sharedPreferences.getBoolean(PrefConstant.IS_LOGGED_IN, false)
        val isBoardedSuccessfully = sharedPreferences.getBoolean(PrefConstant.ON_BOARDED_SUCCESSFULLY, false)
        if (isLoggedIn) {
            val intent: Intent = Intent(this@SplashActivity, MyNotesActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // if not logged in -> check OnBoarding status
            if (!isBoardedSuccessfully) {
                startActivity(Intent(this@SplashActivity, OnBoardingActivity::class.java))
                finish()
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}