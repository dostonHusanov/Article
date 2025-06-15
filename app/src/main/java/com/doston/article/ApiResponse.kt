package com.doston.article

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

data class ApiResponse(
    @Json(name = "ad_top_item") val adTopItem: List<ContentItem>?,
    @Json(name = "ad_down_item") val adDownItem: List<ContentItem>?,
    @Json(name = "recycler_item") val recyclerItem: List<ContentItem>?
)

@JsonClass(generateAdapter = true)
data class ContentItem(
    @Json(name = "contentType")
    val type: String,


    @Json(name = "recordId")
    val articleId: String? = null,
    @Json(name = "showTopAd")
    val showTopAd: String? = null,
    @Json(name = "showBottomAd")
    val showBottomAd: String? = null,

    @Json(name = "bannerId")
    val advertId: String? = null,
    @Json(name = "orgInfo")
    val orgInfo: String? = null,
    @Json(name = "buttonCaption")
    val buttonCaption: String? = null,
    @Json(name = "showAdLabel")
    val showAdLabel: String? = null,

    @Json(name = "heading")
    val title: String? = null,
    @Json(name = "details")
    val description: String? = null,
    @Json(name = "imgPath")
    val imageUrl: String? = null
) : Serializable


fun ContentItem.toArticle(): Article {
    return Article(
        title = this.title ?: "Без заголовка",
        description = this.description ?: "Описание отсутствует",
        imageUrl = this.imageUrl,
        showTopAd = this.showTopAd == "1" || this.showTopAd == "true",
        showBottomAd = this.showBottomAd == "1" || this.showBottomAd == "true"
    )
}

fun ContentItem.toAd(): Ad {
    return Ad(
        id = this.advertId ?: generateAdId(),
        type = this.type,
        title = this.title ?: "Специальное предложение",
        description = this.description ?: "Не упустите выгодную возможность!",
        imageUrl = this.imageUrl,
        showAdLabel = this.showAdLabel == "1" || this.showAdLabel == "true",
        buttonCaption = this.buttonCaption ?: "ПОДРОБНЕЕ",
        orgInfo = this.orgInfo
    )
}


private fun generateAdId(): String {
    return "ad_${System.currentTimeMillis()}_${(1000..9999).random()}"
}

fun ContentItem.isAd(): Boolean {
    return this.type.let { type ->
        type.contains("ad", ignoreCase = true) ||
                type.contains("banner", ignoreCase = true) ||
                type == "inline_ad" ||
                type == "item_ad"
    }
}

fun ContentItem.isArticle(): Boolean {
    return this.type.equals("article", ignoreCase = true)
}

