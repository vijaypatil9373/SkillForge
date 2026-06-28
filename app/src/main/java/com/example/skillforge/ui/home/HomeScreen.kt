package com.example.skillforge.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.skillforge.navigation.NavRoutes
import com.example.skillforge.ui.components.ErrorView
import com.example.skillforge.ui.components.LoadingView
import com.example.skillforge.ui.components.SearchBar
import com.example.skillforge.ui.components.TopSection
import com.example.skillforge.viewmodel.HomeViewModel
import androidx.compose.foundation.clickable

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var selectedCategory by remember { mutableIntStateOf(0) }
    var showAllCategories by remember { mutableStateOf(false) }
    var showAllCourses by remember { mutableStateOf(false) }
    val searchQuery = remember { mutableStateOf("") }

    when {
        state.isLoading -> LoadingView()

        state.error != null -> ErrorView(
            state.error ?: "Something went wrong"
        )

        else -> {
            val safeCategoryIndex =
                selectedCategory.coerceIn(0, state.categories.lastIndex)

            val selectedCategoryData = state.categories[safeCategoryIndex]

            val visibleCategories =
                if (showAllCategories) state.categories else state.categories.take(2)

            val filteredCourses = selectedCategoryData.courses.filter { course ->
                val q = searchQuery.value.trim()

                if (q.isEmpty()) {
                    true
                } else {
                    course.title.contains(q, true) ||
                            course.subtitle.contains(q, true) ||
                            course.instructor.name.contains(q, true) ||
                            course.level.contains(q, true) ||
                            course.tags.any { it.contains(q, true) }
                }
            }

            val visibleCourses =
                if (showAllCourses) filteredCourses else filteredCourses.take(2)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(start = 24.dp, end = 24.dp, top = 28.dp, bottom = 28.dp)
            ) {
                TopSection()

                Spacer(modifier = Modifier.height(26.dp))

                SearchBar(searchQuery)

                Spacer(modifier = Modifier.height(34.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF151515)
                    )

                    Text(
                        text = if (showAllCategories) "Show less" else "See all",
                        color = Color(0xFF14B8A6),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            showAllCategories = !showAllCategories
                        }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(visibleCategories.size) { index ->
                        val category = visibleCategories[index]
                        val realIndex = state.categories.indexOf(category)

                        CategoryCard(
                            category = category,
                            onClick = {
                                selectedCategory = realIndex
                                searchQuery.value = ""
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(34.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Popular courses",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF151515)
                    )

                    Text(
                        text = if (showAllCourses) "Show less" else "See all",
                        color = Color(0xFF14B8A6),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            showAllCourses = !showAllCourses
                        }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (visibleCourses.isEmpty()) {
                    Text(
                        text = "No courses found",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        visibleCourses.forEach { course ->
                            CourseCard(
                                course = course,
                                onClick = {
                                    val realCourseIndex =
                                        selectedCategoryData.courses.indexOf(course)

                                    navController.navigate(
                                        NavRoutes.detail(
                                            safeCategoryIndex,
                                            realCourseIndex
                                        )
                                    )
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(34.dp))

                Text(
                    text = "About Category",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF151515)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = selectedCategoryData.description,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Courses Available : ${selectedCategoryData.courseCount}",
                    color = Color(0xFF14B8A6),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}