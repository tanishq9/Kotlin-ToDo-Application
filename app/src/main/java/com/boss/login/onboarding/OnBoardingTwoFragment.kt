package com.boss.login.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.boss.login.R
import kotlinx.android.synthetic.main.activity_login.*

class OnBoardingTwoFragment : Fragment() {
    lateinit var textViewBack: TextView
    lateinit var textViewNext: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
    }

    private fun bindViews(view: View) {
        textViewBack = view.findViewById(R.id.textViewBack)
        textViewNext = view.findViewById(R.id.textViewNext)
    }

}
