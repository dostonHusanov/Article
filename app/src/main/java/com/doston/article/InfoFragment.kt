package com.doston.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.doston.article.ArticleAdapter.Companion
import com.doston.article.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding
    private val viewModel: MainViewModel by viewModels()
    private val APP_KEY = "com.myapp1"
    private val BASE_URL = "https://dohodinfor.ru/apiv2test/"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(layoutInflater)
        val article = arguments?.getSerializable("article") as? Article
        binding.title.text = article?.title
        val cleanedHtml = cleanHtmlForTextView(article?.description ?: "")
        binding.desc.text =
            android.text.Html.fromHtml(cleanedHtml, android.text.Html.FROM_HTML_MODE_COMPACT)
        val imageUrl = if (article?.imageUrl?.startsWith("http") == true) {
            article.imageUrl
        } else {
            "https://dohodinfor.ru/apiv2test/${article?.imageUrl}"
        }

        binding.image.load(imageUrl) {
            crossfade(true)
            placeholder(R.mipmap.ic_launcher)
            error(R.mipmap.ic_launcher)
        }

        setupAds()

        return binding.root
    }

    private fun setupAds() {
        // Observe top ads (banner ads)
        viewModel.topAds.observe(viewLifecycleOwner) { topAds ->
            if (topAds.isNotEmpty()) {
                val bannerAd = topAds.first() // Get first banner ad
                displayBannerAd(bannerAd)
            } else {
                // Hide banner if no ads available
                binding.banner.visibility = View.GONE
            }
        }

        // Observe bottom ads or recycler items for ordinary ads
        viewModel.recyclerItems.observe(viewLifecycleOwner) { recyclerItems ->
            // Find first ad item from recycler items
            val adItem = recyclerItems.filterIsInstance<RecyclerItem.AdItem>().firstOrNull()
            if (adItem != null) {
                displayOrdinaryAd(adItem.ad)
            } else {
                // Try bottom ads if no ad in recycler items
                viewModel.bottomAds.observe(viewLifecycleOwner) { bottomAds ->
                    if (bottomAds.isNotEmpty()) {
                        val ordinaryAd = bottomAds.first()
                        displayOrdinaryAd(ordinaryAd)
                    } else {
                        // Hide ad card if no ads available
                        binding.itemAdd.visibility = View.GONE
                    }
                }
            }
        }

        // Load articles and ads (you might need to pass appropriate parameters)
        viewModel.loadArticles("defaultUser", "com.myapp1", "defaultTab")
    }

    private fun displayBannerAd(ad: Ad) {
        binding.banner.visibility = View.VISIBLE

        val imageUrl = if (ad.imageUrl?.startsWith("http") == true) {
            ad.imageUrl
        } else {
            "https://dohodinfor.ru/apiv2test/${ad.imageUrl}"
        }

        binding.banner.load(imageUrl) {
            crossfade(true)
            placeholder(R.mipmap.ic_launcher)
            error(R.mipmap.ic_launcher)
        }

        // Set click listener for banner
        binding.banner.setOnClickListener {
            // Handle banner ad click
            handleAdClick(ad, "banner")
        }
    }

    private fun displayOrdinaryAd(ad: Ad) {
        binding.itemAdd.visibility = View.VISIBLE

        // Set ad title
        binding.adTitle.text = ad.title ?: "Специальное предложение"

        // Set ad description
        val cleanedHtml =
            cleanHtmlForTextView(ad.description ?: "Не упустите выгодную возможность!")
        binding.adDescription.text =
            android.text.Html.fromHtml(cleanedHtml, android.text.Html.FROM_HTML_MODE_COMPACT)

        // Set ad image
        val imageUrl = if (ad.imageUrl?.startsWith("http") == true) {
            ad.imageUrl
        } else {
            "https://dohodinfor.ru/apiv2test/${ad.imageUrl}"
        }

        binding.adInline.load(imageUrl) {
            crossfade(true)
            placeholder(R.mipmap.ic_launcher)
            error(R.mipmap.ic_launcher)
        }

        // Show/hide ad label
        binding.adLabel.visibility = if (ad.showAdLabel) View.VISIBLE else View.GONE

        // Set organization info
        if (!ad.orgInfo.isNullOrEmpty()) {
            binding.adOrgInfo.text = ad.orgInfo
            binding.adOrgInfo.visibility = View.VISIBLE
        } else {
            binding.adOrgInfo.visibility = View.GONE
        }

        // Set button
        if (!ad.buttonCaption.isNullOrEmpty()) {
            binding.adButton.text = ad.buttonCaption
            binding.adButtonL.visibility = View.VISIBLE
        } else {
            binding.adButton.text = "ПОДРОБНЕЕ 🔥"
            binding.adButtonL.visibility = View.VISIBLE
        }
        if (ad.id == "ItemAdShortYesBtn") {
            binding.ItemAdShortYesBtn.visibility = View.VISIBLE
            binding.mainL.visibility = View.GONE
            val imageUrl = if (ad.imageUrl?.startsWith("http") == true) {
                ad.imageUrl
            } else {
                "${ArticleAdapter.BASE_URL}${ad.imageUrl}"
            }

            binding.ItemAdShortYesBtn.load(imageUrl) {
                crossfade(true)
                placeholder(R.mipmap.ic_launcher)
                error(R.mipmap.ic_launcher)
            }
        } else if (ad.id == "320*50") {
            binding.itemAdd.visibility = View.GONE
        } else if (ad.id == "InlineShortNoDesc") {
            binding.itemAdd.visibility = View.GONE


        } else if (ad.id == "inlineAd_LongNoBtn") {
            binding.adDescription.visibility = View.GONE


            val imageUrl = if (ad.imageUrl?.startsWith("http") == true) {
                ad.imageUrl
            } else {
                "${Companion.BASE_URL}${ad.imageUrl}"
            }

            binding.adInline.load(imageUrl) {
                crossfade(true)
                placeholder(R.mipmap.ic_launcher)
                error(R.mipmap.ic_launcher)
            }
        } else if (ad.id == "inlineAd_ShortBtn") {
            binding.adDescription.visibility = View.GONE

            val imageUrl = if (ad.imageUrl?.startsWith("http") == true) {
                ad.imageUrl
            } else {
                "${Companion.BASE_URL}${ad.imageUrl}"
            }

            binding.adInline.load(imageUrl) {
                crossfade(true)
                placeholder(R.mipmap.ic_launcher)
                error(R.mipmap.ic_launcher)
            }
        } else {

            val imageUrl = if (ad.imageUrl?.startsWith("http") == true) {
                ad.imageUrl
            } else {
                "${Companion.BASE_URL}${ad.imageUrl}"
            }

            binding.adInline.load(imageUrl) {
                crossfade(true)
                placeholder(R.mipmap.ic_launcher)
                error(R.mipmap.ic_launcher)
            }
        }

        // Set click listeners
        binding.itemAdd.setOnClickListener {
            handleAdClick(ad, "inline")
        }

        binding.adButtonL.setOnClickListener {
            handleAdClick(ad, "button")
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

    private fun cleanHtmlForTextView(html: String): String {
        return html
            .replace(Regex("<h[1-6][^>]*>"), "<b>")
            .replace(Regex("</h[1-6]>"), "</b><br>")
            .replace(Regex("<ul[^>]*>"), "")
            .replace(Regex("</ul>"), "<br>")
            .replace(Regex("<li[^>]*>"), "• ")
            .replace(Regex("</li>"), "<br>")
            .replace(Regex("<p[^>]*>"), "")
            .replace(Regex("</p>"), "<br>")
            .replace(Regex("<([a-zA-Z]+)[^>]*>"), "<$1>")
            .replace(Regex("(<br>\\s*){3,}"), "<br><br>")
            .trim()
    }


}