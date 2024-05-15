package com.example.data.word

import androidx.room.Dao
import androidx.room.Query

@Dao
interface WordDao {
    @Query("SELECT * FROM words")
    suspend fun getAllWords(): List<WordEntity>
}