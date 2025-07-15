package com.practice.surveyapplication.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.surveyapplication.api.ApiState
import com.practice.surveyapplication.data.Record
import com.practice.surveyapplication.data.RecordX
import com.practice.surveyapplication.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RecordRepository
) : ViewModel() {

    // Saving only NewsResponse instead of the Resource wrapper
    val recordList = MutableStateFlow<Record?>(null)

    // Separate variables to handle loading and error states
    val isLoading = MutableStateFlow(true)

    // Separate variables to handle loading and error states
    val errorMessage = MutableStateFlow<String?>(null)

    init {
        getPost()
    }


    private fun getPost() {
        viewModelScope.launch {
            repository.getRecords(
            ).collect { resource ->
                when (resource) {
                    is ApiState.Loading -> {
                        isLoading.value = true // Set loading state
                    }

                    is ApiState.Success -> {
                        isLoading.value = false // Stop loading
                        recordList.value = resource.data // Set news data
                    }

                    is ApiState.Error -> {
                        isLoading.value = false // Stop loading
                        errorMessage.value = resource.message // Set error message
                    }
                }
            }
        }
    }

}