package com.example.skillforge.data.api

import com.example.skillforge.data.model.RootResponse
import retrofit2.http.GET

interface ApiService {

    @GET("notes/refs/heads/main/data.json")
    suspend fun getCourses(): RootResponse
}