package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geoquiz.databinding.ActivityCheatBinding

const val EXTRA_ANSWER_SHOWN = "com.example.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.answer_is_true"

class CheatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheatBinding
    private var answerIsTrue = false
    private var isAnswerShown = false

    // On start of activity do this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View binding logic
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting intent with answer for question
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        // Checking if saved state is not empty, if so load whether user cheated
        // update text and send intent back
        if (savedInstanceState != null){
            isAnswerShown = savedInstanceState.getBoolean(EXTRA_ANSWER_SHOWN, false)
            if (isAnswerShown){
                showAnswer()
                setAnswerShownResult(true)
            }
        }

        // Logic for the cheat show sol button, only shows they cheated if button pressed
        binding.showAnswerButton.setOnClickListener{
            showAnswer()
            isAnswerShown = true
            setAnswerShownResult(true)
        }
    }

    // Overriding saved state to ensure that we track if answer has been shown after rotations
    // Prevents rotations causing cheating
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(EXTRA_ANSWER_SHOWN, isAnswerShown)
    }
    // Helper function for determining if answer is shown and what to give for the intent
    private fun showAnswer(){
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        binding.answerTextView.setText(answerText)
    }

    // Sending specific data back to the MainActivity, to let user know they cheated
    // Specifies which data must be sent back with the intent
    private fun setAnswerShownResult(isAnswerShown: Boolean){
        val data = Intent().apply{
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    // No need for MainActivity or other app code to know what CheatActivity expects as extras on it's Intent
    // Encapsulated work of what is required in here. Similar to static funcs in java, func is static and belongs to class
    companion object{
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}