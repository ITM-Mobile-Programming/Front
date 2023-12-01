package com.hwido.pieceofdayfront.writeNew

import android.annotation.SuppressLint

interface KakaoResponseCallback {

    fun onSuccess(addressName: String)
    fun onError(error: Throwable)
}