package com.practice.surveyapplication.data

data class RecordX(
    val id: String?,
    val options: List<Option>?,
    val question: Question?,
    val referTo: ReferToX?,
    val skip: Skip?,
    val type: String?,
    val validations: Validations?
)