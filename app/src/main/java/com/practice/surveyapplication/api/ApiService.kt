package com.practice.surveyapplication.api

import com.practice.surveyapplication.data.Record
import com.practice.surveyapplication.data.RecordX
import retrofit2.http.GET

interface ApiService {
    companion object{
        const val BASE_URL = "https://api.jsonbin.io/v3/"
    }

    @GET("b/687374506063391d31aca23a")
    suspend fun getRecord(): Record
}