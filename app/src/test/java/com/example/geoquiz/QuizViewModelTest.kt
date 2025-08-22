package com.example.geoquiz

import org.junit.Test
import org.junit.Assert.*
import androidx.lifecycle.SavedStateHandle

class QuizViewModelTest {
    @Test
    // Verifying setup, that our first question is loaded and that it is question number 1
    fun providesExpectedQuestionText(){
        val savedStateHandle = SavedStateHandle()
        val quizViewModel = QuizViewModel(savedStateHandle)
        assertEquals(R.string.question_australia, quizViewModel.currentQuestionText)
    }

    // Checking last question is what we expect and that we can wrap around
    @Test
    fun wrapsAroundQuestionBank(){
        val savedStateHandle = SavedStateHandle(mapOf(CURRENT_INDEX_KEY to 5))
        val quizViewModel = QuizViewModel(savedStateHandle)
        assertEquals(R.string.question_asia, quizViewModel.currentQuestionText)
        quizViewModel.moveToNext()
        assertEquals(com.example.geoquiz.R.string.question_australia, quizViewModel.currentQuestionText)
    }

}