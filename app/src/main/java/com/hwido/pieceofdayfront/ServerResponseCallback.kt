package com.hwido.pieceofdayfront

import org.jetbrains.annotations.ApiStatus.OverrideOnly

interface ServerResponseCallback {
    fun onSuccessSpring(ouPutData: String)

    fun onSuccessSpring(diaryId :Int, hashTags :String, imageUrl:String)

    fun onErrorSpring(error: Throwable)

}