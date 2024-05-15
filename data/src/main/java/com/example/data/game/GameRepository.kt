package com.example.data.game

class GameRepository(private val gameDao: GameDao) {
    suspend fun getGameState(): GameStateEntity? {
        return gameDao.getGameState()
    }

    suspend fun insertGameState(gameStateEntity: GameStateEntity) {
        gameDao.insertGameState(gameStateEntity)
    }
}