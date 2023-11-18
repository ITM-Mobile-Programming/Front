package com.example.pieceofdayfront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginMainpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_mainpage)

        val loginBtn = findViewById<Button>(R.id.login_mainpage_loginButton)
        loginBtn.setOnClickListener {
            val intent = Intent(this, LoginSigninpage::class.java)
            startActivity(intent)
        }
    }
}