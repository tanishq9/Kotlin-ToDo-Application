package com.boss.login.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.boss.login.R
import kotlinx.android.synthetic.main.fragment_on_boarding_one.view.*

class OnBoardingOneFragment : Fragment() {
    lateinit var textViewNext: TextView
    lateinit var onNextClick: OnNextClick

    // this function is called then fragment is attached to the activity
    // typecast the activity's context to interface instance of this fragment
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // typecasting context to OnNextClick
        onNextClick = context as OnNextClick
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding_one, container, false)
    }

    // this function gets immediately called after onCreateView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
    }

    private fun bindViews(view: View) {
        textViewNext = view.findViewById(R.id.textViewNext)
        clickListeners()
    }

    private fun clickListeners() {
        textViewNext.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                onNextClick.onClick()
            }
        })
    }

    interface OnNextClick {
        fun onClick()
    }

}
