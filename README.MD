<h2 align="center">  МИНИСТЕРСТВО НАУКИ И ВЫСШЕГО ОБРАЗОВАНИЯ РОССИЙСКОЙ ФЕДЕРАЦИИ ФЕДЕРАЛЬНОЕ ГОСУДАРСТВЕННОЕ БЮДЖЕТНОЕ ОБРАЗОВАТЕЛЬНОЕ УЧРЕЖДЕНИЕ ВЫСШЕГО ОБРАЗОВАНИЯ «САХАЛИНСКИЙ ГОСУДАРСТВЕННЫЙ УНИВЕРСИТЕТ» </h2>
<div align="center">
<h3>Институт естественных наук и техносферной безопасности
<br>
Кафедра информатики
<br>
Половников Владислав Олегович</h3>

<br>
<h3>Лабораторная работа №6
<br>
“Версии Android SDK и совместимость”
<br>
01.03.02 Прикладная математика и информатика</h3>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<h3 align="right">Научный руководитель
<br>
Соболев Евгений Игоревич
</h3>

<h3 align="center">Южно-Сахалинск
<br>
2023г.
</h3>
<hr>
</div>
<p>
Kotlin - это высокоэффективный современный язык программирования, разработанный компанией JetBrains. Он имеет очень чёткий и лаконичный синтаксис, что делает ваш код лёгким для чтения. Kotlin широко используется во всём мире, и его популярность среди разработчиков постоянно растёт. Многие разработчики, использующие Kotlin, отмечают, что это делает их работу быстрее и увлекательнее.
</p>

<h3 align="center">Задание</h3>

Упражнение. Вывод версии Android на устройстве 
Добавьте в макет GeoQuiz виджет TextView для вывода уровня API устройства, на котором работает программа. На рисунке показано, как должен выглядеть результат.
Результат упражнения Задать текст TextView в макете не получится, потому что версия операционной системы устройства не известна до момента выполнения. Найдите функцию TextView для задания текста в справочной странице TextView в документации Android. Вам нужна функция, получающая один аргумент — строку (или CharSequence). Для настройки размера и гарнитуры текста используйте атрибуты XML, перечисленные в описании TextView.

Упражнение. Ограничение подсказок 
Ограничьте пользователя тремя подсказками. Храните информацию о том, сколько раз пользователь подсматривал ответ, и выводите количество оставшихся подсказок под кнопкой. Если ни одной подсказки не осталось, то кнопка получения подсказки блокируется.

https://www.codewars.com/kata/59377c53e66267c8f6000027
https://www.codewars.com/kata/63b84f54693cb10065687ae5
https://www.codewars.com/kata/566fc12495810954b1000030
https://www.codewars.com/kata/5b180e9fedaa564a7000009a
https://www.codewars.com/kata/55caef80d691f65cb6000040
https://www.codewars.com/kata/55fd2d567d94ac3bc9000064




<h1 align="center">Решение</h2>


https://www.codewars.com/kata/59377c53e66267c8f6000027
![img1](/images/1.png)
https://www.codewars.com/kata/63b84f54693cb10065687ae5
![img2](/images/2.png)
https://www.codewars.com/kata/566fc12495810954b1000030
![img3](/images/3.png)
https://www.codewars.com/kata/5b180e9fedaa564a7000009a
![img4](/images/4.png)
https://www.codewars.com/kata/55caef80d691f65cb6000040
![img5](/images/5.png)
https://www.codewars.com/kata/55fd2d567d94ac3bc9000064
![img6](/images/6.png)


<h2 align="center">MainActivity</h2>

```kotlin
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
        var makeTextResult = R.string.incorrect_toast
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

```

<h2 align="center">QuizViewModel</h2>

```kotlin
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

```


<h2 align="center">activity_main</h2>

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:tools="http://schemas.android.com/tools"
    android:gravity="center"
    android:orientation="vertical">

    <TextView
        android:id="@+id/question_text_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:padding="24dp"
        tools:text="@string/question_australia" />

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

        <Button
            android:id="@+id/true_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/true_button" />
        <Button
            android:id="@+id/false_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/false_button" />
    </LinearLayout>
    <TextView
        android:id="@+id/android_sdk"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:padding="10dp"
        tools:text="@string/android_sdk"/>
    <Button
        android:id="@+id/cheat_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:text="@string/cheat_button" />

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal">
        <ImageButton
            android:id="@+id/prev_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/arrow_left"
            android:contentDescription="Previous"
            />
        <ImageButton
            android:id="@+id/next_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/arrow_right"
            android:contentDescription="Next"/>
    </LinearLayout>

</LinearLayout>

```

<h2 align="center">Landscape activity_main</h2>

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:tools="http://schemas.android.com/tools"
    android:gravity="center"
    android:orientation="horizontal"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/question_text_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="100dp"
        android:padding="24dp"
        tools:text="@string/question_australia"/>

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_vertical|center_horizontal">

        <Button
            android:id="@+id/true_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/true_button" />

        <Button
            android:id="@+id/false_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/false_button" />
    </LinearLayout>

    <TextView
        android:id="@+id/android_sdk"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_vertical|center_horizontal"
        android:layout_marginTop="40dp"
        android:padding="10dp"
        tools:text="@string/android_sdk"/>

    <Button
        android:id="@+id/cheat_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|center_horizontal"
        android:text="@string/cheat_button" />

    <ImageButton
        android:id="@+id/prev_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/arrow_left"
        android:layout_gravity="bottom|left"
        android:contentDescription="Previous question"/>

    <ImageButton
        android:id="@+id/next_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/arrow_right"
        android:layout_gravity="bottom|right"
        android:contentDescription="Next question"/>


</FrameLayout>

```

<h2 align="center">activity_cheat</h2>

```kotlin
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    tools:context="com.example.lab2.CheatActivity">
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="24dp"
        android:text="@string/warning_text"/>
    <TextView
        android:id="@+id/answer_text_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="24dp"
        tools:text="Answer"/>
    <Button
        android:id="@+id/show_answer_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/show_answer_button"/>
</LinearLayout>

```

<h2 align="center">CheatActivity</h2>

```kotlin
package com.example.lab2

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

const val EXTRA_ANSWER_SHOWN = "com.example.lab2.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE =
    "com.example.lab2.answer_is_true"

class CheatActivity : AppCompatActivity() {

    private var answerIsTrue = false
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(true)
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}

```


<h2 align="center">strings</h2>

```kotlin
<resources>
    <string name="app_name">lab5</string>
    <string name="true_button">True</string>
    <string name="false_button">False</string>
    <string name="question_oceans">The Pacific Ocean is larger than the Atlantic Ocean.</string>
    <string name="question_mideast">The Suez Canal connects the Red Sea and the Indian Ocean.</string>
    <string name="question_africa">The source of the Nile River is in Egypt.</string>
    <string name="question_americas">The Amazon River is the longest river in the Americas</string>
    <string name="question_asia">Lake Baikal is tge world\'s oldest and deepest freshwater lake.</string>
    <string name="question_australia">Canberra is the capital of Australia.</string>
    <string name="warning_text">Are you sure you want to do this?</string>
    <string name="show_answer_button">Show Answer</string>
    <string name="cheat_button">Cheat!</string>
    <string name="judgment_toast">Cheating is wrong.</string>
    <string name="correct_toast">Correct!</string>
    <string name="incorrect_toasts">Incorrect</string>
    <string name="android_sdk">Android API</string>
</resources>
```

<h2 align="center">Вывод</h2>
Я научился выводить уровень API и ограничивать количество подсказок у пользователя.
