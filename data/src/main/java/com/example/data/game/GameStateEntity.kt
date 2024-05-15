package com.example.data.game

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
data class GameStateEntity(
    @PrimaryKey val id: Long = 1,  // Utilisation d'un ID fixe pour garantir le remplacement
    @ColumnInfo(name = "remaining_attempts") val remainingAttempts: Int,
    @ColumnInfo(name = "secret_word") val secretWord: String,
    @ColumnInfo(name = "user_input_text") val userInputText: String,
    @ColumnInfo(name = "display_word") val displayWord: String
)