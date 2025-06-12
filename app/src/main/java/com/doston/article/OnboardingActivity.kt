package com.doston.article

import OnboardingAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.doston.article.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: OnboardingAdapter
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isOnboardingCompleted()) {
            goToMain()
            return
        }
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = getColor(R.color.onboarding)
        window.navigationBarColor = getColor(R.color.onboarding)

        viewPager = binding.viewPagerOnboarding

        adapter = OnboardingAdapter(this)
        viewPager.adapter = adapter
        binding.indicator.setViewPager(viewPager)

        binding.next.setOnClickListener {
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.currentItem++
            } else {
                setOnboardingCompleted()
                goToMain()
            }
        }
        binding.skip.setOnClickListener {
            setOnboardingCompleted()
            goToMain()
        }

    }

    private fun setOnboardingCompleted() {
        getSharedPreferences("onboarding", MODE_PRIVATE)
            .edit().putBoolean("completed", true).apply()
    }

    private fun isOnboardingCompleted(): Boolean {
        return getSharedPreferences("onboarding", MODE_PRIVATE)
            .getBoolean("completed", false)
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
