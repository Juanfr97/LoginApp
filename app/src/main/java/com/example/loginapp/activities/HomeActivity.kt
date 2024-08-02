package com.example.loginapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.loginapp.MainActivity
import com.example.loginapp.R

class HomeActivity : AppCompatActivity() {
    private lateinit var logoutBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val sharedPref = getSharedPreferences("mypref", MODE_PRIVATE)
        logoutBtn = findViewById(R.id.logoutBtn)
        logoutBtn.setOnClickListener{
            val editor = sharedPref.edit()
            editor.remove("isLogged")
            editor.apply()

            val intent = Intent(this@HomeActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}