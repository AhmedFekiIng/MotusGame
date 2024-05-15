package com.example.data

import com.example.data.game.GameDao
import com.example.data.game.GameRepository
import com.example.data.game.GameStateEntity
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GameRepositoryTest {

    private val gameDao: GameDao = mockk()
    private lateinit var gameRepository: GameRepository

    @Before
    fun setUp() {
        gameRepository = GameRepository(gameDao)
    }

    @Test
    fun `getGameState returns game state`() = runBlocking {
        val gameState = GameStateEntity(
            remainingAttempts = 3,
            secretWord = "SECRET",
            userInputText = "SECRET",
            displayWord = "S____T"
        )

        coEvery { gameDao.getGameState() } returns gameState

        val result = gameRepository.getGameState()
        assertNotNull(result)
        assertEquals(gameState, result)
    }

    @Test
    fun `insertGameState saves game state`() = runBlocking {
        val gameState = GameStateEntity(
            remainingAttempts = 3,
            secretWord = "SECRET",
            userInputText = "SECRET",
            displayWord = "S____T"
        )

        coEvery { gameDao.insertGameState(gameState) } returns Unit

        gameRepository.insertGameState(gameState)
    }
}