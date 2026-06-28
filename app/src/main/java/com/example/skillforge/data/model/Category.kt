package com.example.skillforge.data.model

data class Category(
    val id: String,
    val name: String,
    val description: String,
    val iconColor: String,
    val courseCount: Int,
    val courses: List<Course>
)