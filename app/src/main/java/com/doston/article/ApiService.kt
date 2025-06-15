package com.doston.article

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("getdata.php")
    fun fetchArticles(@Body request: RequestData): Call<ApiResponse>
}