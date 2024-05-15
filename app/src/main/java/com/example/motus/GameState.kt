package com.example.motus

data class GameState(
    val displayWord: String,
    val displayWordWithoutOrder: String,
    val remainingAttempts: Int,
    val secretWord: String,
    val isCorrect: Boolean = false,
    val userInputText: String = "",
    val correctIndices: List<Int> = emptyList(),
    val misplacedIndices: List<Int> = emptyList(),
    val isValidGuess: Boolean = true
)
