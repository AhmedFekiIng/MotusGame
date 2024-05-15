package com.example.data

import android.util.Log
import com.example.data.api.ApiService
import com.example.data.word.WordDao
import com.example.data.word.WordEntity
import com.example.data.word.WordRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class WordRepositoryImplTest {

    private val apiService: ApiService = mockk()
    private val wordDao: WordDao = mockk()
    private lateinit var wordRepository: WordRepositoryImpl

    @Before
    fun setUp() {
        wordRepository = WordRepositoryImpl(apiService, wordDao)
    }

    @Test
    fun `getWordList returns words from database when not empty`() = runBlocking {
        val wordEntities = listOf(WordEntity(1, "example"))
        coEvery { wordDao.getAllWords() } returns wordEntities

        val words = wordRepository.getWordList()
        assertEquals(listOf("example"), words)
    }

    @Test
    fun `getWordList fetches words from API when database is empty`() = runBlocking {
        val mockResponseBody = ResponseBody.create(null, "word1\nword2\nword3")
        val mockResponse = Response.success(mockResponseBody)

        coEvery { wordDao.getAllWords() } returns emptyList()
        coEvery { apiService.getWordList() } returns mockResponse

        val words = wordRepository.getWordList()
        assertEquals(listOf("word1", "word2", "word3"), words)
    }

    @Test
    fun `getWordList returns empty list when API fails`() = runBlocking {
        val wordDao = mockk<WordDao>()
        val apiService = mockk<ApiService>()
        coEvery { wordDao.getAllWords() } returns emptyList()
        // Mock ApiService to return an unsuccessful response
        coEvery { apiService.getWordList() } returns Response.error(404, ResponseBody.create(null, ""))
        // Mock the Log.e method to return 0 (success)
        mockkStatic(Log::class)
        coEvery { Log.e(any(), any(), any<Throwable>()) } returns 0

        val wordRepository = WordRepositoryImpl(apiService, wordDao)
        val words = wordRepository.getWordList()
        assertTrue(words.isEmpty())
    }
}