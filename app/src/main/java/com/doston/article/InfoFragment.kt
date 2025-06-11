package com.doston.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.doston.article.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding

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

        return binding.root
    }
    private fun cleanHtmlForTextView(html: String): String {
        return html
            // Remove complex HTML tags that don't work well in TextView
            .replace(Regex("<h[1-6][^>]*>"), "<b>")
            .replace(Regex("</h[1-6]>"), "</b><br>")
            .replace(Regex("<ul[^>]*>"), "")
            .replace(Regex("</ul>"), "<br>")
            .replace(Regex("<li[^>]*>"), "• ")
            .replace(Regex("</li>"), "<br>")
            .replace(Regex("<p[^>]*>"), "")
            .replace(Regex("</p>"), "<br>")
            // Remove any remaining complex attributes
            .replace(Regex("<([a-zA-Z]+)[^>]*>"), "<$1>")
            // Clean up multiple line breaks
            .replace(Regex("(<br>\\s*){3,}"), "<br><br>")
            // Trim
            .trim()
    }



}