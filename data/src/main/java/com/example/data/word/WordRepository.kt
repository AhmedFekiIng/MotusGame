package com.example.data.word

interface WordRepository {
    suspend fun getWordList(): List<String>
}