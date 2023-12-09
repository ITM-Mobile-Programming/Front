package com.hwido.pieceofdayfront.writeNew

import android.app.Application
import androidx.room.Room

class MyApplication : Application() {
    companion object {
        lateinit var database: DiaryDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            DiaryDatabase::class.java,
            "diary_database"
        ).build()
    }
}