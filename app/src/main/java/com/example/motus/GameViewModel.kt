package com.example.motus

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.game.GameRepository
import com.example.data.game.GameStateEntity
import com.example.domain.MotusGameInteractor
import com.example.data.word.WordRepository
import com.example.domain.MotusGameImpl
import kotlinx.coroutines.launch

class GameViewModel(
    private val motusGame: MotusGameInteractor,
    private val gameRepository: GameRepository,
    private val wordRepository: WordRepository
) : ViewModel() {
    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState>
        get() = _gameState
    private var secretWord: String = ""
    private var remainingAttempts: Int = MotusGameImpl.MAX_ATTEMPTS
    private var userInputText by mutableStateOf("")
    private var wordList: List<String> = emptyList()
    private var displayWord: String? = null

    init {
        viewModelScope.launch {
            wordList = wordRepository.getWordList()
            val gameStateEntity = gameRepository.getGameState()
            if (gameStateEntity != null) {
                // Load existing game state from DataBase
                remainingAttempts = gameStateEntity.remainingAttempts
                secretWord = gameStateEntity.secretWord
                userInputText = gameStateEntity.userInputText
                displayWord = gameStateEntity.displayWord
                motusGame.initializeState(secretWord, remainingAttempts)
                Log.d("GameViewModel", "Loaded game state: secretWord=$secretWord, remainingAttempts=$remainingAttempts")
            } else {
                // Start a new game if no existing state
                startNewGame()
            }
            updateGameState()
        }
    }

    fun startNewGame() {
        viewModelScope.launch {
            try {
                secretWord = motusGame.startNewGame(wordList)
                remainingAttempts = MotusGameImpl.MAX_ATTEMPTS
                motusGame.initializeState(secretWord, remainingAttempts)
                updateGameState()
                saveGameState()
                Log.d("GameViewModel", "Started new game: secretWord=$secretWord, remainingAttempts=$remainingAttempts")
            } catch (e: Exception) {
                Log.e("GameViewModel", "Failed to start new game: ${e.message}", e)
            }
        }
    }

    fun setInputText(text: String) {
        userInputText = text
        saveGameState()
    }


    fun checkGuess() {
        viewModelScope.launch {
            try {
                val guessResult = motusGame.checkGuess(userInputText, wordList)
                remainingAttempts = guessResult.remainingAttempts
                _gameState.value = GameState(
                    displayWord = guessResult.displayWord,
                    displayWordWithoutOrder = guessResult.displayWordWithoutOrder,
                    remainingAttempts = remainingAttempts,
                    secretWord = secretWord,
                    isCorrect = guessResult.isCorrect,
                    userInputText = userInputText,
                    correctIndices = guessResult.correctIndices,
                    misplacedIndices = guessResult.misplacedIndices,
                    isValidGuess = guessResult.isValidGuess
                )
                Log.d("GameViewModel", "Checked guess: remainingAttempts=$remainingAttempts")
                saveGameState()
            } catch (e: Exception) {
                Log.e("Exception GameViewModel", "Failed to check guess: ${e.message}", e)
            }
        }
    }

    private fun getInitialDisplayWord(secretWord: String): String {
        return secretWord.substring(0, 1) + "_ ".repeat(secretWord.length - 1)
    }

    private fun updateGameState() {
        _gameState.value = GameState(
            displayWord = displayWord ?: getInitialDisplayWord(secretWord),
            displayWordWithoutOrder = "",
            remainingAttempts = remainingAttempts,
            secretWord = secretWord,
            userInputText = userInputText
        )
    }


    fun saveGameState() {
        viewModelScope.launch {
            val currentState = _gameState.value
            if (currentState != null) {
                val gameStateEntity = GameStateEntity(
                    id = 1,
                    remainingAttempts = currentState.remainingAttempts,
                    secretWord = currentState.secretWord,
                    userInputText = currentState.userInputText,
                    displayWord = currentState.displayWord
                )
                gameRepository.insertGameState(gameStateEntity)
                Log.d("GameViewModel", "Saving game state: secretWord=${currentState.secretWord}, remainingAttempts=${currentState.remainingAttempts}")
            }
        }
    }
}