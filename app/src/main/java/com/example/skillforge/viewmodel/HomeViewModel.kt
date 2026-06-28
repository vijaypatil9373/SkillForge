package com.example.skillforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillforge.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = CourseRepository()

    private val _uiState =
        MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> =
        _uiState

    init {

        loadCourses()

    }

    private fun loadCourses() {

        viewModelScope.launch {

            try {

                val data =
                    repository.getCategories()

                _uiState.value =
                    HomeUiState(

                        isLoading = false,

                        categories = data

                    )

            } catch (e: Exception) {

                _uiState.value =
                    HomeUiState(

                        isLoading = false,

                        error = e.message

                    )

            }

        }

    }

}