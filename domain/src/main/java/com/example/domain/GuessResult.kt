package com.example.domain

data class GuessResult(
    val displayWord: String = "",
    val displayWordWithoutOrder: String = "",
    val isCorrect: Boolean = false,
    val correctIndices: List<Int> = emptyList(),
    val misplacedIndices: List<Int> = emptyList(),
    val remainingAttempts: Int = 7,
    val isValidGuess: Boolean = true,
    val isCorrectLength: Boolean = true
)
