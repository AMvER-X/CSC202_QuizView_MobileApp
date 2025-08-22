package com.example.geoquiz

/*
Data clas for storing data format for each question
*/

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean)