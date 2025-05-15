package com.example.cursoandroidclase2.data.db

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    lateinit var db: AppDatabase

    fun init(context: Context) {
        db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "posts-db"
        ).build()
    }
}