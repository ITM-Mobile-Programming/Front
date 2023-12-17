package com.hwido.pieceofdayfront.ServerAPI


import com.hwido.pieceofdayfront.DT.DiaryEntry

interface ServerResponseCallback {
    fun onSuccessSpring(ouPutData: String)

    fun onSuccessSpringDiaryList(diaryList : List<DiaryEntry>)

    fun onSuccessSpring(diaryId :Int, hashTags :String, imageUrl:String)

    fun onErrorSpring(error: Throwable)

}