package com.doston.article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doston.article.databinding.ItemAdBinding
import com.doston.article.databinding.ItemBinding

class ArticleAdapter(
    private val list: List<RecyclerItem>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ARTICLE = 1
        private const val TYPE_AD = 2
        private const val BASE_URL = "https://dohodinfor.ru/apiv2test/"
        private const val APP_KEY = "com.myapp1"
    }

    class ArticleViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)
    class AdViewHolder(val binding: ItemAdBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is RecyclerItem.AdItem -> TYPE_AD
            is RecyclerItem.ArticleItem -> TYPE_ARTICLE

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            TYPE_ARTICLE -> {
                val binding =
                    ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ArticleViewHolder(binding)
            }

            TYPE_AD -> {
                val binding =
                    ItemAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AdViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArticleViewHolder -> bindArticle(holder, list[position] as RecyclerItem.ArticleItem)
            is AdViewHolder -> {
                bindAd(holder, list[position] as RecyclerItem.AdItem)

                val layoutParams =
                    holder.itemView.layoutParams as androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams
                layoutParams.isFullSpan = true
                holder.itemView.layoutParams = layoutParams
            }
        }
    }

    private fun bindArticle(holder: ArticleViewHolder, item: RecyclerItem.ArticleItem) {
        val article = item.article
        holder.binding.title.text = article.title

        val cleanedHtml = cleanHtmlForTextView(article.description ?: "")
        holder.binding.desc.text =
            android.text.Html.fromHtml(cleanedHtml, android.text.Html.FROM_HTML_MODE_COMPACT)

        val imageUrl = if (article.imageUrl?.startsWith("http") == true) {
            article.imageUrl
        } else {
            "$BASE_URL${article.imageUrl}"
        }

        holder.binding.image.load(imageUrl) {
            crossfade(true)
            placeholder(R.mipmap.ic_launcher)
            error(R.mipmap.ic_launcher)
        }

        holder.binding.root.setOnClickListener {
            onClickListener.onClick(article)
        }
    }

    private fun bindAd(holder: AdViewHolder, item: RecyclerItem.AdItem) {
        val ad = item.ad

        holder.binding.adTitle.text = ad.title ?: "alo Специальное предложение"
        val cleanedHtml =
            cleanHtmlForTextView(ad.description ?: " alo Не упустите выгодную возможность!")
        holder.binding.adDescription.text =
            android.text.Html.fromHtml(cleanedHtml, android.text.Html.FROM_HTML_MODE_COMPACT)


        holder.binding.adLabel.visibility = if (ad.showAdLabel) View.VISIBLE else View.GONE

        if (!ad.orgInfo.isNullOrEmpty()) {
            holder.binding.adOrgInfo.text = ad.orgInfo
            holder.binding.adOrgInfo.visibility = View.VISIBLE
        } else {
            holder.binding.adOrgInfo.visibility = View.GONE
        }


        if (ad.buttonCaption.isNullOrEmpty()) {
            holder.binding.adButton.visibility = View.GONE
        } else {
            holder.binding.adButton.text = "ПОДРОБНЕЕ 🔥"
            holder.binding.adButton.visibility = View.VISIBLE
        }

        if (ad.id == "320*50") {
            holder.binding.adImage.visibility = View.VISIBLE
            holder.binding.adInline.visibility = View.GONE
            holder.binding.adTitle.visibility = View.GONE
            holder.binding.adDescription.visibility = View.GONE
            holder.binding.adButtonL.visibility = View.GONE
            holder.binding.adLabel.visibility = View.GONE
            holder.binding.adOrgInfo.visibility = View.GONE
            val imageUrl = if (ad.imageUrl?.startsWith("http") == true) {
                ad.imageUrl
            } else {
                "$BASE_URL${ad.imageUrl}"
            }

            holder.binding.adImage.load(imageUrl) {
                crossfade(true)
                placeholder(R.mipmap.ic_launcher)
                error(R.mipmap.ic_launcher)
            }
        } else if (ad.id == "ItemAdShortYesBtn") {
            holder.binding.image.visibility = View.VISIBLE
            holder.binding.mainL.visibility = View.GONE
            val imageUrl = if (ad.imageUrl?.startsWith("http") == true) {
                ad.imageUrl
            } else {
                "$BASE_URL${ad.imageUrl}"
            }

            holder.binding.image.load(imageUrl) {
                crossfade(true)
                placeholder(R.mipmap.ic_launcher)
                error(R.mipmap.ic_launcher)
            }

        } else if (ad.id == "InlineShortNoDesc") {
            holder.binding.itemId.visibility = View.GONE
            holder.binding.mainL.visibility = View.GONE
            holder.binding.image.visibility = View.GONE

        } else {
            holder.binding.adImage.visibility = View.GONE
            val imageUrl = if (ad.imageUrl?.startsWith("http") == true) {
                ad.imageUrl
            } else {
                "$BASE_URL${ad.imageUrl}"
            }

            holder.binding.adInline.load(imageUrl) {
                crossfade(true)
                placeholder(R.mipmap.ic_launcher)
                error(R.mipmap.ic_launcher)
            }
        }


        holder.binding.root.setOnClickListener {
            onClickListener.onAdClick(ad, "inline")
        }

        holder.binding.adButton.setOnClickListener {
            onClickListener.onAdClick(ad, "button")
        }
    }


    private fun extractDiscountFromText(text: String): String {
        val discountRegex = Regex("(\\d+)%")
        val match = discountRegex.find(text)
        return match?.value ?: "70%"
    }

    private fun parseDiscountText(discountText: String): Pair<String, String> {
        return if (discountText.contains("%")) {
            val number = discountText.replace("%", "")
            Pair(number, "OFF")
        } else {
            Pair("70", "OFF")
        }
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

    interface OnClickListener {
        fun onClick(article: Article)
        fun onAdClick(ad: Ad, placement: String)
    }
}