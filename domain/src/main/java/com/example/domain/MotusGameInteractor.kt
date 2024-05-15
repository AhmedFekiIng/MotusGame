package com.example.domain

interface MotusGameInteractor {
    suspend fun startNewGame(wordList: List<String>): String
    suspend fun checkGuess(guess: String, wordList: List<String>): GuessResult
    fun initializeState(secretWord: String, remainingAttempts: Int)
}
