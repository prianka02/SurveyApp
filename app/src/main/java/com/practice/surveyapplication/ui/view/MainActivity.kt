package com.practice.surveyapplication.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.surveyapplication.ui.theme.SurveyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SurveyApplicationTheme {
                HomeSurvey(
                )
            }
        }
    }
}

@Composable
fun HomeSurvey(
    viewModel: MainViewModel = hiltViewModel()
) {

    // Observe the states from the ViewModel
    val isLoading by viewModel.isLoading.collectAsState()
    val recordList by viewModel.recordList.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    recordList?.record?.let {
        Text(
            text = it.toString()
    )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SurveyApplicationTheme {
//     }
//}