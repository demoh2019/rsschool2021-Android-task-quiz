package com.rsschool.quiz.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.rsschool.quiz.R
import com.rsschool.quiz.contract.navigator
import com.rsschool.quiz.databinding.FragmentResultBinding


class Result : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val result = arguments?.getInt(RESULT_COUNT) ?: 0
        binding.textView.text = getString(R.string.result, result)
        binding.back.setOnClickListener { restart() }
        binding.exit.setOnClickListener { exit() }
        binding.share.setOnClickListener { share() }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic private val RESULT_COUNT = "RESULT_COUNT"
        @JvmStatic
        fun newInstance(result: Int) =
            Result().apply {
                arguments = bundleOf(RESULT_COUNT to result)
            }
    }

    fun share(){
        navigator().share()
    }

    fun restart() {
        navigator().restart()
    }

    fun exit() {
        navigator().exit()
    }
}