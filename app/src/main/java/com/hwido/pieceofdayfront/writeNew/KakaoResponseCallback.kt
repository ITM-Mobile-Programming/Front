package com.hwido.pieceofdayfront.writeNew

interface KakaoResponseCallback {

    fun onSuccessLocation(ouPutData: String)
    fun onErrorLocation(error: Throwable)
}