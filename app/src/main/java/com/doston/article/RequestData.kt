package com.doston.article

import androidx.annotation.Keep

@Keep
data class RequestData(val user: String, val key: String, val tab: String)
