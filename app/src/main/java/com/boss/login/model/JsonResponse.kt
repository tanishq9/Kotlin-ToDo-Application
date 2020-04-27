package com.boss.login.model

data class JsonResponse(val status_code: String, val message: String, val data: List<Data>)

data class Data(val title: String, val description: String, val author: String, val img_url: String, val blog_url: String, val publishedAt: String)