package com.example.lab2

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_CURRENTINDEX = "current_index"
private const val KEY_COUNTANSWER = "count_answer"
private const val KEY_CORRECTANSWER = "correct_answer"
private const val KEY_GETANSWER = "get_answer"
private const val KEY_ISCHEATER = "is_cheater"
private const val KEY_CHEATANSWER = "cheat_answer"
private const val KEY_COUNTHINTS = "count_hints"
private const val REQUEST_CODE_CHEAT = 0



class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var prevButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var androidSDK: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"OnCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            quizViewModel.currentIndex = savedInstanceState.getInt(KEY_CURRENTINDEX,0)
            quizViewModel.countAnswers = savedInstanceState.getInt(KEY_COUNTANSWER,0)
            quizViewModel.countCorrectAnswers = savedInstanceState.getDouble(KEY_CORRECTANSWER,0.0)
            quizViewModel.getAnswer = savedInstanceState?.getBooleanArray(KEY_GETANSWER) ?: BooleanArray(6)
            quizViewModel.isCheater = savedInstanceState.getBoolean(KEY_ISCHEATER,false)
            quizViewModel.cheatAnswer = savedInstanceState?.getBooleanArray(KEY_CHEATANSWER) ?: BooleanArray(6)
            quizViewModel.countHints = savedInstanceState.getInt(KEY_COUNTHINTS,0)
        }


        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        androidSDK = findViewById(R.id.android_sdk)


        androidSDK.setText("API Level " + Build.VERSION.SDK_INT)

        cheatButton.setOnClickListener { view ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer()
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
            val options = ActivityOptions
                .makeClipRevealAnimation(view, 0, 0, view.width, view.height)
        }

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }
        nextButton.setOnClickListener {
            quizViewModel.currentIndex = (quizViewModel.currentIndex + 1) % quizViewModel.questionBank.size
            questionUpdate()
        }

        questionTextView.setOnClickListener {
            quizViewModel.currentIndex = (quizViewModel.currentIndex + 1) % quizViewModel.questionBank.size
            questionUpdate()
        }

        prevButton.setOnClickListener{
            if (quizViewModel.currentIndex == 0) {
                quizViewModel.currentIndex = quizViewModel.questionBank.size - 1
            }
            else{
                quizViewModel.currentIndex--
            }
            questionUpdate()
        }
        questionUpdate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.countHints++
            quizViewModel.cheatAnswer[quizViewModel.currentIndex] =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onStart(){
        super.onStart()
        Log.d(TAG, "OnStart() called")
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG, "OnResume() called")
    }

    override fun onPause(){
        super.onPause()
        Log.d(TAG, "OnPause called")
    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_CURRENTINDEX, quizViewModel.currentIndex)
        savedInstanceState.putDouble(KEY_CORRECTANSWER, quizViewModel.countCorrectAnswers)
        savedInstanceState.putInt(KEY_COUNTANSWER, quizViewModel.countAnswers)
        savedInstanceState.putBooleanArray(KEY_GETANSWER,quizViewModel.getAnswer)
        savedInstanceState.putBoolean(KEY_ISCHEATER,quizViewModel.isCheater)
        savedInstanceState.putBooleanArray(KEY_CHEATANSWER,quizViewModel.cheatAnswer)
        savedInstanceState.putInt(KEY_COUNTHINTS, quizViewModel.countHints)
    }
    override fun onStop(){
        super.onStop()
        Log.d(TAG, "OnStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnDestroy() called")
    }

    private fun questionUpdate(){
        val questionTextResId = quizViewModel.questionBank[quizViewModel.currentIndex].textResId
        questionTextView.setText(questionTextResId)
        if(quizViewModel.countHints == 3)
        {
            cheatButton.isEnabled = false
        }
        if(quizViewModel.getAnswer[quizViewModel.currentIndex]){
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        }
        else{
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.questionBank[quizViewModel.currentIndex].answer
        trueButton.isEnabled = false
        falseButton.isEnabled = false
        quizViewModel.getAnswer[quizViewModel.currentIndex] = true
        quizViewModel.countAnswers++

        if (quizViewModel.cheatAnswer[quizViewModel.currentIndex]) {
            Toast.makeText(this, R.string.judgment_toast,Toast.LENGTH_SHORT).show()
        }
        var makeTextResult = R.string.incorrect_toasts
        if (userAnswer == correctAnswer) {
            quizViewModel.countCorrectAnswers++
            makeTextResult = R.string.correct_toast
        }
        Toast.makeText(this, makeTextResult, Toast.LENGTH_SHORT).show()

        if (quizViewModel.countAnswers == quizViewModel.questionBank.size){

            val value = (quizViewModel.countCorrectAnswers/(quizViewModel.questionBank.size))*100

            var result = "Correct answers is: " + Math.round(value) + "%"
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show()
        }

    }

}