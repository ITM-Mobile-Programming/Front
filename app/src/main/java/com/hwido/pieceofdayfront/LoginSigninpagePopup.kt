package com.hwido.pieceofdayfront

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button

class LoginSigninpagePopup(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.login_signinpage_popup)

        findViewById<Button>(R.id.signin_check_popup_confirm).setOnClickListener {
            dismiss()

            val intent = Intent(context, MainMainpage::class.java)
            context.startActivity(intent)
        }
    }
}