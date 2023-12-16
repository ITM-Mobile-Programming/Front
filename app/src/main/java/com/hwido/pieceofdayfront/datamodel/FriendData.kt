package com.hwido.pieceofdayfront.datamodel

data class FriendResponse(
    val statusCode: Int,
    val message: String,
    val data: List<FriendData>
)

data class FriendData(
    val friendId: Int,
    val memberId: Int,
    val nickName: String,
    val profileUrl: String
)

data class SharedDiaryItem(
    val diaryImage: String,
    val writer: String,
    val date: String
)