package com.hwido.pieceofdayfront.writeNew

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Insert
    suspend fun insert(diaryEntry: DiaryEntry)

    @Query("SELECT * FROM diary_table")
    fun getAllDiaries(): Flow<List<DiaryEntry>>

    @Query("SELECT * FROM diary_table WHERE date = :date")
    fun getDiaryByDate(date: String): LiveData<List<DiaryEntry>>
}