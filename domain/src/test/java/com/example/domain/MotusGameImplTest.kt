package com.example.domain

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MotusGameImplTest {

    private lateinit var motusGame: MotusGameImpl

    @Before
    fun setUp() {
        motusGame = MotusGameImpl()
    }

    @Test
    fun `startNewGame initializes secretWord and resets attempts`() = runBlocking {
        val wordList = listOf("EXAMPLE", "TESTING", "MOTUSGAME")
        val secretWord = motusGame.startNewGame(wordList)

        assertTrue(wordList.map { it.uppercase() }.contains(secretWord))
        assertEquals(MotusGameImpl.MAX_ATTEMPTS, motusGame.getRemainingAttempts())
    }

    @Test
    fun `checkGuess returns invalid result if guess is not in wordList`() = runBlocking {
        val wordList = listOf("EXAMPLE", "TESTING", "MOTUSGAME")
        motusGame.startNewGame(wordList)

        val result = motusGame.checkGuess("invalid", wordList)

        assertFalse(result.isValidGuess)
        assertEquals(MotusGameImpl.MAX_ATTEMPTS, motusGame.getRemainingAttempts()) // Initialize the Remaining attempts
    }

    @Test
    fun `checkGuess decrements attempts left`() = runBlocking {
        val wordList = listOf("EXAMPLE", "TESTING", "MOTUSGAME")
        motusGame.startNewGame(wordList)

        val initialAttempts = motusGame.getRemainingAttempts()

        motusGame.checkGuess("TESTING", wordList)
        val remainingAttempts = motusGame.getRemainingAttempts()

        assertEquals(initialAttempts - 1, remainingAttempts)
    }

    @Test
    fun `checkGuess returns correct result when guess is correct`() = runBlocking {
        val secretWord = "TESTING"
        motusGame.initializeState(secretWord, MotusGameImpl.MAX_ATTEMPTS)

        val result = motusGame.checkGuess(secretWord, listOf(secretWord))

        assertTrue(result.isCorrect)
        assertEquals(secretWord, result.displayWord)
        assertEquals(secretWord, result.displayWordWithoutOrder)
    }

    @Test
    fun `checkGuess returns correct result when guess is incorrect`() = runBlocking {
        val secretWord = "EXAMPLE"
        motusGame.initializeState(secretWord, MotusGameImpl.MAX_ATTEMPTS)

        val result = motusGame.checkGuess("TESTING", listOf(secretWord, "TESTING"))

        assertFalse(result.isCorrect)
        assertNotEquals(secretWord, result.displayWord)
    }

    @Test
    fun `initializeState sets secretWord and remainingAttempts`() {
        val secretWord = "TESTING"
        val attempts = 3
        motusGame.initializeState(secretWord, attempts)

        assertEquals(secretWord, motusGame.getSecretWord())
        assertEquals(attempts, motusGame.getRemainingAttempts())
    }

    @Test
    fun `getDisplayWord returns correct masked word`() = runBlocking {
        val secretWord = "TESTING"
        motusGame.initializeState(secretWord, MotusGameImpl.MAX_ATTEMPTS)

        val displayWord = motusGame.checkGuess("TASTING", listOf(secretWord, "TASTING")).displayWord

        assertEquals("T_STING", displayWord)
    }

    @Test
    fun `getDisplayWord returns correct masked word with misplaced letters`() = runBlocking {
        val secretWord = "EXAMPLE"
        motusGame.initializeState(secretWord, MotusGameImpl.MAX_ATTEMPTS)

        val result = motusGame.checkGuess("EXAMLPE", listOf(secretWord, "EXAMLPE"))

        assertEquals("EXA_MLE", result.displayWordWithoutOrder)
        assertFalse(result.isCorrect)
    }
}