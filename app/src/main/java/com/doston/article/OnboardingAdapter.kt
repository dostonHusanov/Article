import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doston.article.OnboardingFragment
import com.doston.article.R

class OnboardingAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragments = listOf(
        OnboardingFragment.newInstance(R.mipmap.ic_launcher, "Welcome", "This is the first screen."),
        OnboardingFragment.newInstance(R.mipmap.ic_launcher, "Explore", "Learn what this app can do."),
        OnboardingFragment.newInstance(R.mipmap.ic_launcher, "Start", "Let’s begin your journey!")
    )

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}
