package com.rsschool.quiz.contract

import androidx.fragment.app.Fragment


fun Fragment.navigator(): IQuestion{
    return requireActivity() as IQuestion
}

interface IQuestion {
    fun showFragment()
    fun goBack()
    fun exit()
    fun saveQuestion(answer: Int)
    fun share()
    fun restart()
}