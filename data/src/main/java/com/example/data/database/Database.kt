package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.game.GameDao
import com.example.data.game.GameStateEntity
import com.example.data.word.WordDao
import com.example.data.word.WordEntity

@Database(entities = [WordEntity::class, GameStateEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var instance: com.example.data.database.Database? = null

        fun getInstance(context: Context): com.example.data.database.Database {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): com.example.data.database.Database {
            return Room.databaseBuilder(context, com.example.data.database.Database::class.java, "database")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
