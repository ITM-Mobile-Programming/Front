package com.example.pieceofdayfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginSigninpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_signinpage)

        findViewById<View>(R.id.login_signinpage_signinBtn).setOnClickListener {
            showPopup()
        }
    }

    private fun showPopup() {
        val popup = LoginSigninCheckPopup(this)
        popup.show()

    }
}