package com.boss.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    lateinit var editTextFullName: EditText
    lateinit var editTextUserName: EditText
    lateinit var buttonLogin: Button
    lateinit var sharedPreferences: SharedPreferences
    val tag: String = "Activity:Login"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(tag, "onCreate Called")
        setContentView(R.layout.activity_detail)
        bindView()
        setUpSharedPreferences()

        buttonLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val fullName = editTextFullName.text.toString()
                val userName = editTextUserName.text.toString()
                if (fullName.isNotEmpty() && userName.isNotEmpty()) {
                    val intent = Intent(this@LoginActivity, MyNotesActivity::class.java)
                    intent.putExtra(AppConstant.FULL_NAME, fullName)
                    startActivity(intent)
                    // after login, save login status
                    saveLoginStatus();
                    // save full name
                    saveFullName(fullName);
                    // finish this activity
                    finish();
                } else {
                    Toast.makeText(this@LoginActivity, "Enter both the field(s)", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun saveFullName(fullName: String) {
        sharedPreferences.edit().putString(PrefConstant.FULL_NAME, fullName).apply()
    }

    private fun saveLoginStatus() {
        sharedPreferences.edit().putBoolean(PrefConstant.IS_LOGGED_IN, true).apply()
    }

    private fun setUpSharedPreferences() {
        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private fun bindView() {
        editTextFullName = findViewById(R.id.editTextFullName)
        editTextUserName = findViewById(R.id.editTextUserName)
        buttonLogin = findViewById(R.id.buttonLogin)
    }
}