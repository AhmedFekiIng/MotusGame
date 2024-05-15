package com.example.domain

class MotusGameImpl() : MotusGameInteractor {
    private var secretWord: String = ""
    private var attemptsLeft: Int = MAX_ATTEMPTS

    override suspend fun startNewGame(wordList: List<String>): String {
        secretWord = wordList.filter { it.length in 6..8 }.random().uppercase()
        attemptsLeft = MAX_ATTEMPTS
        return secretWord
    }

    override suspend fun checkGuess(guess: String, wordList: List<String>): GuessResult {
        require(guess.length == secretWord.length)
        val isValidGuess = wordList.contains(guess)
        if (!isValidGuess) return GuessResult(isValidGuess = false)

        attemptsLeft--

        // Vérifier si le mot deviné est correct
        val isCorrect = guess.equals(secretWord, ignoreCase = true)

        // Calculer les indices des lettres correctes et mal placées
        val correctIndices = mutableListOf<Int>()
        val misplacedIndices = mutableListOf<Int>()

        for (i in guess.indices) {
            if (guess[i] == secretWord[i]) {
                correctIndices.add(i)
            } else if (secretWord.contains(guess[i])) {
                misplacedIndices.add(i)
            }
        }

        return GuessResult(
            displayWord = getDisplayWord(guess, true),
            displayWordWithoutOrder = getDisplayWord(guess, false, misplacedIndices),
            isCorrect = isCorrect,
            correctIndices = correctIndices,
            misplacedIndices = misplacedIndices,
            remainingAttempts = attemptsLeft,
            isValidGuess = true
        )
    }

    private fun getDisplayWord(guess: String, exactWord: Boolean, misplacedIndices: List<Int>? = null): String {
        val result = StringBuilder()
        for (i in secretWord.indices) {
            val char = secretWord[i]
            val displayChar = when {
                // We should always display the first Char
                i == 0 && exactWord -> char
                guess[i] == char && exactWord -> char
                (guess[i] == char || (misplacedIndices != null && i in misplacedIndices)) && !exactWord -> guess[i]
                else -> "_"
            }
            result.append(displayChar)
        }
        return result.toString()
    }

    fun getRemainingAttempts(): Int {
        return attemptsLeft
    }

    fun getSecretWord(): String {
        return secretWord
    }

    override fun initializeState(secretWord: String, remainingAttempts: Int) {
        this.secretWord = secretWord
        this.attemptsLeft = remainingAttempts
    }

    companion object {
        const val MAX_ATTEMPTS = 7
    }
}