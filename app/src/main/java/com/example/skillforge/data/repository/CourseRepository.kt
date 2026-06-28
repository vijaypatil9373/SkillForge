package com.example.skillforge.repository

import com.example.skillforge.api.RetrofitInstance

class CourseRepository {

    suspend fun getCategories() =
        RetrofitInstance.api.getCourses().categories

}