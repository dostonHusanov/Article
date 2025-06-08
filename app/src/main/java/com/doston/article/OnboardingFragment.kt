package com.doston.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.doston.article.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(layoutInflater)


        arguments?.let {
            binding.imageOnboarding.setImageResource(it.getInt(ARG_IMAGE))
            binding.titleOnboarding.text = it.getString(ARG_TITLE)
            binding.descOnboarding.text = it.getString(ARG_DESC)
        }

        return binding.root
    }
}
