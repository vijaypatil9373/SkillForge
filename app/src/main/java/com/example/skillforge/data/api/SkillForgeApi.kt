package com.example.skillforge.api

import com.example.skillforge.data.model.ApiResponse
import retrofit2.http.GET

interface SkillForgeApi {

    @GET("android-assesment/notes/refs/heads/main/data.json")
    suspend fun getCourses(): ApiResponse
}