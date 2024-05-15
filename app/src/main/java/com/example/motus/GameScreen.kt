package com.example.motus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.motus.composable.DisplayWordAnnotated
import com.example.motus.composable.GameHeader
import com.example.motus.composable.GameInstructions
import com.example.motus.composable.GameResultMessage
import com.example.motus.composable.GameStatus
import com.example.motus.composable.GuessButton
import com.example.motus.composable.GuessInputField
import com.example.motus.composable.PlayAgainButton

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameState by gameViewModel.gameState.observeAsState()

    var userInputText by remember { mutableStateOf("") }

    LaunchedEffect(userInputText) {
        gameViewModel.setInputText(userInputText)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEDEDED))
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            val (header, instructions, status, displayWord, inputField, guessButton, resultMessage, playAgainButton) = createRefs()

            GameHeader(modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            Spacer(modifier = Modifier.height(16.dp))

            gameState?.let { state ->
                GameInstructions(
                    secretWordLength = state.secretWord.length,
                    modifier = Modifier.constrainAs(instructions) {
                        top.linkTo(header.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                if(!state.displayWord.isNullOrEmpty())
                    GameStatus(
                    displayWord = state.displayWord.toCharArray().joinToString(" "),
                    attemptsLeft = state.remainingAttempts,
                    modifier = Modifier.constrainAs(status) {
                        top.linkTo(instructions.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                DisplayWordAnnotated(
                    state,
                    modifier = Modifier.constrainAs(displayWord) {
                        top.linkTo(instructions.bottom, margin = 130.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (state.isCorrect) {
                    GameResultMessage(
                        message = "Congratulations, you guessed it!",
                        modifier = Modifier.constrainAs(resultMessage) {
                            top.linkTo(displayWord.bottom, margin = 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                    PlayAgainButton(
                        onClick = {
                            gameViewModel.startNewGame()
                            gameViewModel.saveGameState()
                        },
                        modifier = Modifier.constrainAs(playAgainButton) {
                            top.linkTo(resultMessage.bottom, margin = 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                } else if (state.remainingAttempts > 0 && state.isValidGuess) {
                    GuessInputField(
                        userInputText,
                        onUserInputChange = { userInputText = it },
                        modifier = Modifier.constrainAs(inputField) {
                            top.linkTo(displayWord.bottom, margin = 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                    GuessButton(
                        enabled = userInputText.isNotBlank(),
                        onClick = {
                            gameViewModel.checkGuess()
                            gameViewModel.saveGameState()
                        },
                        modifier = Modifier.constrainAs(guessButton) {
                            top.linkTo(inputField.bottom, margin = 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                } else {
                    GameResultMessage(
                        message = if (!state.isValidGuess) {
                            "Game Over! This word is misspelled or missing from the dictionary. The word was ${state.secretWord}"
                        } else {
                            "Game Over! You ran out of attempts. The word was ${state.secretWord}"
                        },
                        color = Color.Red,
                        modifier = Modifier.constrainAs(resultMessage) {
                            top.linkTo(displayWord.bottom, margin = 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                    PlayAgainButton(
                        onClick = {
                            gameViewModel.startNewGame()
                            gameViewModel.saveGameState()
                        },
                        modifier = Modifier.constrainAs(playAgainButton) {
                            top.linkTo(resultMessage.bottom, margin = 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                }
            }
        }
    }
}