package com.hwido.pieceofdayfront.datamodel

data class getDiaryResponse(
    val statusCode: Int,
    val message: String,
    val data: List<DiaryEntry>
)

data class DiaryEntry(
    val diaryId: Int,
    val title: String,
    val context: String,
    val location: String,
    val weatherCode: String,
    val thumbnailUrl: String,
    val hashTagList: List<HashTag>
    //MBTI가 있는지?
)

data class HashTag(
    val createdDate: Long,
    val updatedDate: Long,
    val diaryToHashTagId: Int,
    val hashTag: String
)

data class DateDiary(
    val writtenDate: String
)