package com.example.cookingguide.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.cookingguide.R
import com.example.cookingguide.utils.Common

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Common.download({
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }, 500)

        },{
            Toast.makeText(this,"Lỗi mạng",Toast.LENGTH_SHORT).show()
            finish()
        })
    }
}