package com.example.skillforge.navigation

object NavRoutes {
    const val HOME = "home"
    const val DETAIL = "detail/{categoryIndex}/{courseIndex}"
    const val PLAYER = "player/{categoryIndex}/{courseIndex}/{lessonIndex}"

    fun detail(categoryIndex: Int, courseIndex: Int) =
        "detail/$categoryIndex/$courseIndex"

    fun player(categoryIndex: Int, courseIndex: Int, lessonIndex: Int) =
        "player/$categoryIndex/$courseIndex/$lessonIndex"
}