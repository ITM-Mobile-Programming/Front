package com.hwido.pieceofdayfront.writeNew.Location

interface KakaoResponseCallback {

    fun onSuccessLocation(ouPutData: String)
    fun onErrorLocation(error: Throwable)
}