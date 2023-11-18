package com.example.pieceofdayfront

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button

class LoginSigninCheckPopup(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.login_signin_check_popup)

        findViewById<Button>(R.id.signin_check_popup_confirm).setOnClickListener {
            dismiss()

        }
    }
}