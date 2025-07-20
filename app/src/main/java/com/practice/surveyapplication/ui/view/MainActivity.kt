package com.practice.surveyapplication.ui.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.surveyapplication.data.RecordX
import com.practice.surveyapplication.ui.theme.SurveyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SurveyApplicationTheme {
                HomeSurvey()
            }
        }
    }
}


@Composable
fun HomeSurvey(viewModel: MainViewModel = hiltViewModel()) {
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val recordList by viewModel.recordList.collectAsState()

    val listState = rememberLazyListState()
    val scrollTargetIndex by viewModel.scrollTargetIndex.collectAsState()

    val displayedQuestions = recordList?.record ?: emptyList()

    val scrollState = rememberScrollState()

    LaunchedEffect(scrollTargetIndex) {
        scrollTargetIndex?.let {
            listState.animateScrollToItem(it)
        }
    }

    if (isLoading) {
        Text("Loading...", modifier = Modifier.padding(16.dp))
        return
    }

    if (errorMessage != null) {
        Text("Error: $errorMessage", color = Color.Red, modifier = Modifier.padding(16.dp))
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(modifier = Modifier
            .padding(top = 40.dp)
            .align(Alignment.CenterHorizontally),
            text = "Survey Application",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .verticalScroll(scrollState)
//                .padding(16.dp)
//        ) {
//
//            recordList?.record?.forEach { question ->
//                 RenderQuestion(
//                    question = question, viewModel = viewModel,
//                             onAnswered = { nextId ->
//                         if (nextId != null) {
//                             viewModel.showNextQuestionById(nextId)
//                         }
//                     }
//                 )
//                Spacer(modifier = Modifier.height(24.dp))
//            }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            itemsIndexed(displayedQuestions, key = { _, q -> q.id!! }) { index, question ->
                RenderQuestion(
                    question = question,
                    viewModel = viewModel,
                    onAnswered = { nextId ->
                        // Trigger scroll to next question by ID
                        val nextIndex = displayedQuestions.indexOfFirst { it.id == nextId }
                        if (nextIndex != -1) {
                            viewModel.scrollToQuestion(nextIndex)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
//                        val answers = viewModel.getAnswers() // define this function in ViewModel
                        // Handle form submission here (e.g. log answers or call API)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit Answers")
                }

                Spacer(modifier = Modifier.height(64.dp))
            }
        }

//
//            if (displayedQuestions.lastOrNull()?.referTo?.id == "submit") {
//                Button(onClick = {
//                    val answers = viewModel.getAnswers()
//                    // Handle submission here
//                }) {
//                    Text("Submit Answers")
//                }
//            }
        }
    }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderQuestion(
    question: RecordX,
    viewModel: MainViewModel,
    onAnswered: (String?) -> Unit
) {
    val context = LocalContext.current
    val selectedRadioBtn = viewModel.selectedRadioBtn[question.id]
//    var storeNumberTxt = viewModel.storeNumberTxt[question.id]

//    Text(
//        text = question.question?.slug ?: "",
//        style = MaterialTheme.typography.bodyMedium
//    )
    Column {
        question.question?.slug?.let {

            Text(text = "*$it", fontWeight = FontWeight.Bold) }
//        question.options?.forEach { option ->
//            Text(text = "• $option")
//        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    when (question.type) {

        "multipleChoice" -> {
//            val selectedOption = remember { mutableStateOf<String?>(null) }

            question.options?.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            question.id?.let { viewModel.setRadioBtn(it, option.value ?: "") }
                            onAnswered(option.referTo?.id)
                        }
                        .padding(8.dp)
                ) {
                    RadioButton(
                        selected = selectedRadioBtn == option.value,
                        onClick = {
                            question.id?.let { viewModel.setRadioBtn(it, option.value ?: "") }
                            onAnswered(option.referTo?.id)
                        }
                    )
                    option.value?.let {
                        Text(text = it, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

        }

        "numberInput", "textInput" -> {
            var text by remember {
                mutableStateOf(viewModel.storeNumberTxt[question.id] ?: "")
            }
            val isValid = remember(text) {
                question.validations?.regex?.toRegex()?.matches(text) ?: true
            }

            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current

            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    question.id?.let { it1 -> viewModel.storeNumberTxt(it1, it) }
                },
                label = { Text("Enter a number (Between 1–15)") },
                isError = !isValid,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (isValid) {
                            focusManager.clearFocus()
                            keyboardController?.hide() // Hide keyboard
                            onAnswered(question.referTo?.id) // Refer to next question
                        }
                    }
                )
            )
        }

        "dropdown" -> {
            DropdownQuestion(question = question, viewModel = viewModel, onAnswered = onAnswered)
        }
        "checkbox" -> {
            val selected = remember { mutableStateListOf<String>() }

            question.options?.forEach { option ->
                val isChecked = selected.contains(option.value)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isChecked) selected.remove(option.value)
                            else option.value?.let { selected.add(it) }
                        }
                        .padding(8.dp)
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            if (it) option.value?.let { it1 -> selected.add(it1) }
                            else selected.remove(option.value)
                        }
                    )
                    option.value?.let { Text(it, modifier = Modifier.padding(start = 8.dp)) }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
//                    onAnswered(question.referTo?.id)
                          },
                enabled = selected.isNotEmpty()
            ) {
                Text("Next")
            }
        }

        "camera" -> {
            Button(
                onClick = {
                    // TODO: Add real camera capture logic
//                    onAnswered(question.referTo?.id)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Capture Photo")
            }
        }

        else -> {
            Text("Unsupported question type: ${question.type}")
        }
    }
}



@Composable
fun DropdownQuestion(
    question: RecordX,
    viewModel: MainViewModel,
    onAnswered: (String?) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedOption = remember { mutableStateOf(viewModel.selectDropdown[question.id]) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {

        Text(text = question.question?.slug ?: "Select an option", style = MaterialTheme.typography.bodyLarge)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            OutlinedTextField(
                value = selectedOption.value ?: "",
                onValueChange = {},
                label = { Text("Select") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded.value = true }
            )

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier
                    .fillMaxWidth(),
                properties = PopupProperties(
                    focusable = true,
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true,
                    clippingEnabled = false // Key fix for LazyColumn
                )
            ) {
                question.options?.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.value ?: "") },
                        onClick = {
                            val value = option.value ?: ""
                            selectedOption.value = value
                            expanded.value = false
                            question.id?.let { viewModel.selectedDropDown(it, value) }
                            onAnswered(option.referTo?.id)
                        }
                    )
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SurveyApplicationTheme {
//     }
//}