package com.rsschool.quiz

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.rsschool.quiz.`data-base`.Questions
import com.rsschool.quiz.`data-base`.Question
import com.rsschool.quiz.contract.IQuestion
import com.rsschool.quiz.fragment.Quiz
import com.rsschool.quiz.fragment.Result


class MainActivity : AppCompatActivity(), IQuestion {
    private var _questions = Questions().data

    private val fragmentCreated = object : FragmentManager.FragmentLifecycleCallbacks(){
        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
            super.onFragmentStopped(fm, f)
            changeTheme()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openQuiz()
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCreated, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCreated)
    }

    override fun onBackPressed() {
        if (getCurrentQuestion() == 1 || getCurrentQuestion() == 6)
            return
        super.onBackPressed()
    }

    private fun openQuiz(){
        supportFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.fragmentContainer, Quiz.newInstance(getQuestion()))
        }
    }

    private fun openRusult(result: Int){
        supportFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.fragmentContainer, Result.newInstance(result))
        }
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun exit() {
        finishAndRemoveTask()
    }

    override fun showFragment() {
        val question = getCurrentQuestion()
        if (question >= _questions.size)
            openRusult(getResult())
        else
            openQuiz()
    }

    fun getResult(): Int{
        var result = 0
        for(question in _questions)
            if (question.answer == question.corret)
                result++
        return result
    }

    override fun saveQuestion(answer: Int) {
        val indexQuestion = getCurrentQuestion() - 1
        _questions[indexQuestion].answer = answer
    }

    override fun share() {
        val sendIntent = Intent().apply{
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Result")
            putExtra(Intent.EXTRA_TEXT, getShareResult())
        }
        val shareIntent = Intent.createChooser(sendIntent, "Result")
        startActivity(shareIntent)
    }

    override fun restart() {
        _questions = Questions().data
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        openQuiz()
    }

    private fun getShareResult(): String{
        var result = ""
        for(question in _questions) {
            result += getString(R.string.result, getResult()) + "\n"
            result += question.number.toString() + ")" + question.Question + "\n"
            for((index, answer) in question.answers.withIndex())
                result += (index + 1).toString() + ")" + answer + "\n"
            result += "Выбранный вариант: " + question.answer.toString() + "\n"
        }
        return result
    }

    private fun getCurrentQuestion():Int {
        return supportFragmentManager.backStackEntryCount
    }

    private fun getQuestion(): Question{
        return _questions.get(getCurrentQuestion())
    }

    private fun changeTheme(){
        val theme = mapOf(
            0 to R.style.Theme_Quiz_First,
            1 to R.style.Theme_Quiz_Second, 2 to R.style.Theme_Quiz_Third,
            3 to R.style.Theme_Quiz_Fourth, 4 to R.style.Theme_Quiz_Five,
        )

        val indexQuestion = getCurrentQuestion() - 1
        theme[indexQuestion]?.let {
            setTheme(it)
            window.statusBarColor = getColorFromAttr(android.R.attr.statusBarColor)
        }
    }

    fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }
}