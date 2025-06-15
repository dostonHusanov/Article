package com.doston.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.doston.article.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayout

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var currentTab = "money"

    companion object {
        private const val APP_KEY = "com.myapp1"
        private const val BASE_URL = "https://dohodinfor.ru/apiv2test/"
        private const val TAB_MONEY = "money"
        private const val TAB_OTHER = "other"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabs()
        setupRecyclerView()
        loadContent()
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        currentTab = TAB_MONEY
                        Log.d("MainFragment", "Money tab selected")
                    }

                    1 -> {
                        currentTab = TAB_OTHER
                        Log.d("MainFragment", "Other tab selected")
                    }
                }
                loadContent()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

                loadContent()
            }
        })


        binding.tabLayout.getTabAt(0)?.select()
    }

    private fun setupRecyclerView() {

        val staggeredGridLayoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(
            2,
            androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
        )
        binding.recyclerview.layoutManager = staggeredGridLayoutManager

        viewModel.recyclerItems.observe(viewLifecycleOwner) { items ->
            hideProgressBar()
            if (items.isNotEmpty()) {
                val adapter = ArticleAdapter(
                    items,
                    object : ArticleAdapter.OnClickListener {
                        override fun onClick(article: Article) {
                            navigateToArticle(article)
                        }

                        override fun onAdClick(ad: Ad, placement: String) {
                            handleAdClick(ad, placement)
                        }
                    }
                )
                binding.recyclerview.adapter = adapter
                Log.d(
                    "MainFragment",
                    "RecyclerView updated with ${items.size} items for tab: $currentTab"
                )
            } else {
                Log.d("MainFragment", "No items to display for tab: $currentTab")
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        }
    }

    private fun loadContent() {
        Log.d("MainFragment", "Loading content for tab: $currentTab")
        showProgressBar()
        viewModel.loadArticles(
            user = "caf31ffd-cb31-48e6-a67b-cfe794c57486",
            key = APP_KEY,
            tab = currentTab
        )
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.nestedScrollView.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.nestedScrollView.visibility = View.VISIBLE
    }

    private fun navigateToArticle(article: Article) {
        try {
            val bundle = bundleOf(
                "article" to article,
                "topAds" to viewModel.topAds.value?.toTypedArray(),
                "bottomAds" to viewModel.bottomAds.value?.toTypedArray()
            )
            findNavController().navigate(R.id.infoFragment, bundle)
        } catch (e: Exception) {
            Log.e("MainFragment", "Navigation error", e)
            Toast.makeText(context, "Ошибка открытия статьи", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleAdClick(ad: Ad, placement: String) {
        try {

            val clickUrl =
                "${BASE_URL}gt.php?app_key=$APP_KEY&survey_id=${ad.id}&placement=$placement"

            Log.d("AdClick", "Opening ad URL: $clickUrl")
            Log.d("AdClick", "Ad details - ID: ${ad.id}, Type: ${ad.type}, Placement: $placement")

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(clickUrl))
            startActivity(intent)

            Toast.makeText(context, "Переход к рекламе...", Toast.LENGTH_SHORT).show()

            trackAdClick(ad, placement)

        } catch (e: Exception) {
            Log.e("MainFragment", "Error handling ad click", e)
            Toast.makeText(context, "Ошибка открытия рекламы", Toast.LENGTH_SHORT).show()
        }
    }

    private fun trackAdClick(ad: Ad, placement: String) {
        Log.d("Analytics", "Ad clicked - ID: ${ad.id}, Type: ${ad.type}, Placement: $placement")
    }
}