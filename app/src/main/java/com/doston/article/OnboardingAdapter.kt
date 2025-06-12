import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doston.article.OnboardingFragment
import com.doston.article.R

class OnboardingAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragments = listOf(
        OnboardingFragment.newInstance(
            R.drawable.onboarding_a,
            "Share with anyone",
            "Give others access to any file or any folder you choose"
        ),
        OnboardingFragment.newInstance(
            R.drawable.onboarding_b,
            "Access everywhere",
            "Reach your files from any device, anywhere"
        ),
        OnboardingFragment.newInstance(
            R.drawable.onboarding_c,
            "Save everything",
            "Files in drive are kept safe, so you can't lose them"
        )
    )

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}
