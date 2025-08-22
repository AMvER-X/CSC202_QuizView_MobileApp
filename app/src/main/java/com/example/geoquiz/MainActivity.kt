package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    // Variable for ViewBinding
    private lateinit var binding: ActivityMainBinding // For view binding

    // Delegation of quizViewModel with latebinding done by android
    private val quizViewModel: QuizViewModel by viewModels()

    // Working with ActivityResults API for getting results back from the CheatActivity
    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handling of result here
        if (result.resultCode == Activity.RESULT_OK){
            quizViewModel.isCheater = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    // On creation of app, do this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tag for logging
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        // Checks whether answer is right
        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true);
        }

        // Checks whether answer is wrong
        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false);
        }

        // Logic for cheat button
        binding.cheatButton.setOnClickListener{
            // Starting cheat activity (Intent to communicate with OS for another activity)
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            //startActivity(intent) //replaced with contract
            cheatLauncher.launch(intent)
        }

        // Logic for next button
        binding.nextButton.setOnClickListener{
            quizViewModel.moveToNext()
            updateQuestion()
            quizViewModel.isCheater = false
        }

        // Logic for prev button
        binding.prevButton.setOnClickListener{
            quizViewModel.moveToPrev()
            updateQuestion()
            quizViewModel.isCheater = false
        }
        updateQuestion()
    }

    override fun onStart(){
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }


    // Handles updating of text on screen with index appropriate index of current question
    private fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }

    // Logic to check whether the question is right or wrong
    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer: Boolean = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }
}