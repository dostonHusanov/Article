package com.doston.article

import com.squareup.moshi.Json
import java.io.Serializable

data class Article(
    @Json(name = "heading") val title: String?,
    @Json(name = "details") val description: String?,
    @Json(name = "imgPath") val imageUrl: String?,
    val showTopAd: Boolean = false,
    val showBottomAd: Boolean = false
) : Serializable

data class Ad(
    val id: String,

    val type: String,
    val title: String?,
    var description: String?,
    val imageUrl: String?,
    val showAdLabel: Boolean = false,
    val buttonCaption: String?,
    val orgInfo: String?
) : Serializable

sealed class RecyclerItem {
    data class ArticleItem(val article: Article) : RecyclerItem()
    data class AdItem(val ad: Ad) : RecyclerItem()
}