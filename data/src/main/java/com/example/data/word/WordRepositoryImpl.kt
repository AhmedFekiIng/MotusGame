package com.example.data.word

import android.util.Log
import com.example.data.api.ApiService
import okhttp3.ResponseBody
import retrofit2.Response



class WordRepositoryImpl(
    private val apiService: ApiService,
    private val wordDao: WordDao
): WordRepository {

    override suspend fun getWordList(): List<String> {
        val wordEntities = wordDao.getAllWords()
        return if (wordEntities.isNotEmpty()) {
            wordEntities.map { it.word }
        } else {
            fetchWordListFromApi()
        }
    }

    private suspend fun fetchWordListFromApi(): List<String> {
        return try {
            val response: Response<ResponseBody> = apiService.getWordList()
            if (response.isSuccessful) {
                parseWordList(response.body()?.string())
            } else {
                Log.e("API Exception", "Failed to fetch word list: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("API Exception", "Failed to fetch word list: ${e.message}", e)
            emptyList()
        }
    }

    private fun parseWordList(response: String?): List<String> {
        return response?.lines()?.filter { it.isNotBlank() } ?: emptyList()
    }
}