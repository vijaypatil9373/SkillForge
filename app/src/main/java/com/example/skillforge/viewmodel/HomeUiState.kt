package com.example.skillforge.viewmodel

import com.example.skillforge.data.model.Category

data class HomeUiState(

    val isLoading:Boolean=true,

    val categories:List<Category> = emptyList(),

    val error:String?=null

)