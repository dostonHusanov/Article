package com.doston.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    val recyclerItems = MutableLiveData<List<RecyclerItem>>()
    val topAds = MutableLiveData<List<Ad>>()
    val bottomAds = MutableLiveData<List<Ad>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadArticles(user: String, key: String, tab: String) {
        _isLoading.value = true

        val request = RequestData(user, key, tab)
        RetrofitClient.api.fetchArticles(request).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    val recyclerItemsList = apiResponse?.recyclerItem ?: emptyList()
                    val mixedItems = recyclerItemsList.map { contentItem ->
                        when (contentItem.type) {
                            "article" -> {
                                Log.d(
                                    "API_PARSE",
                                    "Article: articleId=${contentItem.articleId}, title=${contentItem.title}"
                                )
                                RecyclerItem.ArticleItem(contentItem.toArticle())
                            }

                            "inline_ad", "item_ad", "banner" -> {
                                Log.d(
                                    "API_PARSE",
                                    "Ad: advertId=${contentItem.advertId}, title=${contentItem.title}"
                                )
                                RecyclerItem.AdItem(contentItem.toAd())
                            }

                            else -> {
                                Log.d(
                                    "API_PARSE",
                                    "Unknown type: ${contentItem.type}, treating as article"
                                )
                                RecyclerItem.ArticleItem(contentItem.toArticle())
                            }
                        }
                    }


                    val topAdsList = apiResponse?.adTopItem?.map {
                        Log.d("API_PARSE", "Top Ad: advertId=${it.advertId}, title=${it.title}")
                        it.toAd()
                    } ?: emptyList()

                    val bottomAdsList = apiResponse?.adDownItem?.map {
                        Log.d("API_PARSE", "Bottom Ad: advertId=${it.advertId}, title=${it.title}")
                        it.toAd()
                    } ?: emptyList()

                    Log.d(
                        "API_SUCCESS",
                        "Fetched ${recyclerItemsList.size} recycler items, ${topAdsList.size} top ads, ${bottomAdsList.size} bottom ads"
                    )

                    recyclerItems.postValue(mixedItems)
                    topAds.postValue(topAdsList)
                    bottomAds.postValue(bottomAdsList)
                } else {
                    Log.e("API_ERROR", "Response not successful: ${response.code()}")
                    recyclerItems.postValue(emptyList())
                    topAds.postValue(emptyList())
                    bottomAds.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _isLoading.value = false

                Log.e("MainViewModel", "API call failed", t)
                recyclerItems.postValue(emptyList())
                topAds.postValue(emptyList())
                bottomAds.postValue(emptyList())
            }
        })
    }
}