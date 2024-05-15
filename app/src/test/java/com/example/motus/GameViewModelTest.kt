package com.example.motus

import com.example.data.game.GameRepository
import com.example.data.word.WordRepository
import com.example.domain.MotusGameImpl
import com.example.domain.MotusGameInteractor
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GameViewModelTest {
    private lateinit var gameViewModel: GameViewModel
    private val mockMotusGame: MotusGameInteractor = mockk()
    private val mockGameRepository: GameRepository = mockk()
    private val mockWordRepository: WordRepository = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher()) // Initialisation du dispatcher principal pour les tests
        gameViewModel = GameViewModel(mockMotusGame, mockGameRepository, mockWordRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

//    @Test
//    fun `startNewGame updates game state`() = runTest {
//        // Mock behavior of dependencies
//        val secretWord = "testing"
//        coEvery { mockMotusGame.startNewGame(listOf("testing","game")) } returns secretWord
//        assertEquals(secretWord, secretWord)
//        assertEquals(MotusGameImpl.MAX_ATTEMPTS, 7) // This Unit test need to be fixed later
//    }
}