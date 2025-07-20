package com.practice.surveyapplication.ui.view

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.surveyapplication.api.ApiState
import com.practice.surveyapplication.data.Record
import com.practice.surveyapplication.data.RecordX
import com.practice.surveyapplication.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RecordRepository
) : ViewModel() {

    val recordList = MutableStateFlow<Record?>(null)

    // Track currently displayed questions
    private val _displayedQuestions = MutableStateFlow<List<RecordX>>(emptyList())
    val displayedQuestions = _displayedQuestions

    val isLoading = MutableStateFlow(true)

    val errorMessage = MutableStateFlow<String?>(null)

    private val _scrollTargetIndex = MutableStateFlow<Int?>(null)
    val scrollTargetIndex: StateFlow<Int?> = _scrollTargetIndex


    private val _selectedRadioBtn = mutableStateMapOf<String, String>()
    val selectedRadioBtn: Map<String, String> = _selectedRadioBtn

    private val _storeNumberTxt = mutableStateMapOf<String, String>()
    val storeNumberTxt: Map<String, String> = _storeNumberTxt

    private val _selectDropdown = mutableStateMapOf<String, String>()
    val selectDropdown: Map<String, String> = _selectDropdown

    fun setRadioBtn(questionId: String, answer: String) {
        _selectedRadioBtn[questionId] = answer
    }

    fun scrollToQuestion(index: Int) {
        _scrollTargetIndex.value = index
    }

    fun storeNumberTxt(questionId: String, answer: String){
        _storeNumberTxt[questionId] = answer
    }

    fun selectedDropDown(questionId: String, answer: String){
        _selectDropdown[questionId] = answer
    }

    init {
        getRecord()
    }


    private fun getRecord() {
        viewModelScope.launch {
            repository.getRecords(
            ).collect { resource ->
                when (resource) {
                    is ApiState.Loading -> {
                        isLoading.value = true
                    }

                    is ApiState.Success -> {
                        isLoading.value = false
                        recordList.value = resource.data // Set records data

                        // Start with the first question
                        resource.data.record?.firstOrNull()?.let { first ->
                            _displayedQuestions.value = listOf(first)
                        }
                    }

                    is ApiState.Error -> {
                        isLoading.value = false // Stop loading
                        errorMessage.value = resource.message // Set error message
                    }
                }
            }
        }
    }

    // Add next question by id (called when a choice is selected)
    fun showNextQuestionById(nextId: String) {
        val allQuestions = recordList.value?.record ?: return
        val next = allQuestions.find { it.id == nextId } ?: return

        if (!_displayedQuestions.value.contains(next)) {
            _displayedQuestions.value = _displayedQuestions.value + next
        }
    }

}