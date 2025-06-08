package com.doston.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class OnboardingFragment : Fragment() {

    companion object {
        private const val ARG_IMAGE = "image"
        private const val ARG_TITLE = "title"
        private const val ARG_DESC = "desc"

        fun newInstance(imageRes: Int, title: String, desc: String): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle().apply {
                putInt(ARG_IMAGE, imageRes)
                putString(ARG_TITLE, title)
                putString(ARG_DESC, desc)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding, container, false)

        arguments?.let {
            view.findViewById<ImageView>(R.id.imageOnboarding).setImageResource(it.getInt(ARG_IMAGE))
            view.findViewById<TextView>(R.id.titleOnboarding).text = it.getString(ARG_TITLE)
            view.findViewById<TextView>(R.id.descOnboarding).text = it.getString(ARG_DESC)
        }

        return view
    }
}
