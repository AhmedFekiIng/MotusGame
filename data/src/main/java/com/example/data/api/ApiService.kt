package com.example.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("iys4katchh")
    suspend fun getWordList(): Response<ResponseBody>
}
