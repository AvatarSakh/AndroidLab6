package com.example.lab2

import androidx.lifecycle.ViewModel


private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    val questionBank = listOf(
        Question(R.string.question_australia,true),
        Question(R.string.question_oceans,true),
        Question(R.string.question_mideast,false),
        Question(R.string.question_africa,false),
        Question(R.string.question_americas,true),
        Question(R.string.question_asia,true))

    var currentIndex = 0
    var countAnswers = 0
    var countCorrectAnswers = 0.0
    var getAnswer = BooleanArray(questionBank.size)
    var isCheater = false
    var cheatAnswer = BooleanArray(questionBank.size)
    var countHints = 0

    fun currentQuestionAnswer(): Boolean{
        return questionBank[currentIndex].answer
    }
}