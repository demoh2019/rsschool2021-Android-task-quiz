package com.rsschool.quiz.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.os.bundleOf
import com.rsschool.quiz.R
import com.rsschool.quiz.`data-base`.Question
import com.rsschool.quiz.contract.navigator
import com.rsschool.quiz.databinding.FragmentQuizBinding


class Quiz : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        val questionCurrent = arguments?.getSerializable(QUESTION) as? Question
        questionCurrent?.let {
            with(binding) {
                toolbar.title = getString(R.string.headerTitle, it.number + 1)
                question.text = getString(R.string.nameQuestion, it.Question)
                optionOne.text = getString(R.string.optionFirst, it.answers[0])
                optionTwo.text = getString(R.string.option_second, it.answers[1])
                optionThree.text = getString(R.string.option_third, it.answers[2])
                optionFour.text = getString(R.string.optoion_four, it.answers[3])
                optionFive.text = getString(R.string.option_five, it.answers[4])

                it.answer?.let {
                    val radioButton = radioGroup.getChildAt(it) as? RadioButton
                    radioButton?.isChecked = true
                }

                if (it.number == 0) {
                    previousButton.isEnabled = false
                    toolbar.navigationIcon = null
                }

                if (radioGroup.checkedRadioButtonId == -1)
                    nextButton.isEnabled = false

                if(it.number == 4)
                    nextButton.text = getString(R.string.submit)

                nextButton.setOnClickListener {
                    showFragment()
                }
                previousButton.setOnClickListener { goBack() }

                radioGroup.setOnCheckedChangeListener { _, checkedId ->
                    val selectButton = radioGroup.findViewById<RadioButton>(checkedId)
                    val indexButton = radioGroup.indexOfChild(selectButton)
                    saveQuestion(indexButton)
                    nextButton.isEnabled = true
                }

                toolbar.setNavigationOnClickListener { goBack() }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun goBack() {
        navigator().goBack()
    }

    fun showFragment() {
        navigator().showFragment()
    }

    fun saveQuestion(answer: Int) {
        navigator().saveQuestion(answer)
    }

    companion object {
        @JvmStatic var QUESTION = "QUESTION"
        @JvmStatic
        fun newInstance(question: Question) =
            Quiz().apply {
                arguments = bundleOf(QUESTION to question)
            }
    }
}