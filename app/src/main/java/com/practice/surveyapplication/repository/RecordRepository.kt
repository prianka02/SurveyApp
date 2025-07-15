package com.practice.surveyapplication.repository

import android.util.Log
import com.practice.surveyapplication.api.ApiService
import com.practice.surveyapplication.api.ApiState
import com.practice.surveyapplication.data.Record
import com.practice.surveyapplication.data.RecordX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RecordRepository @Inject constructor (private val apiService: ApiService) {

    suspend fun getRecords(): Flow<ApiState<Record>> = flow {
        Log.d("Repository", "getPosts")
        try {
            Log.d("Repository", "get")

            emit(ApiState.Loading) // Emit loading state
            val response = apiService.getRecord() // Make the network request
            Log.d("Repository", response.toString())
            emit(ApiState.Success(response))
        } catch (e: Exception) {
            emit(ApiState.Error(e.message ?: "Unknown Error")) // Emit error state
            Log.d("Error Catch", "getPosts")
        }
    }.flowOn(Dispatchers.IO)
}