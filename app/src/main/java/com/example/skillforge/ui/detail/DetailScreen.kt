package com.example.skillforge.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.skillforge.navigation.NavRoutes
import com.example.skillforge.ui.components.ErrorView
import com.example.skillforge.ui.components.LoadingView
import com.example.skillforge.viewmodel.HomeViewModel

@Composable
fun DetailScreen(
    navController: NavController,
    categoryIndex: Int,
    courseIndex: Int,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var isBookmarked by remember { mutableStateOf(false) }
    var isFollowing by remember { mutableStateOf(false) }

    when {
        state.isLoading -> LoadingView()
        state.error != null -> ErrorView(state.error ?: "Error")

        else -> {
            val safeCategoryIndex = categoryIndex.coerceIn(0, state.categories.lastIndex)
            val safeCourseIndex =
                courseIndex.coerceIn(0, state.categories[safeCategoryIndex].courses.lastIndex)
            val course = state.categories[safeCategoryIndex].courses[safeCourseIndex]

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                item {
                    Box(modifier = Modifier.padding(20.dp)) {
                        AsyncImage(
                            model = course.thumbnailUrl,
                            contentDescription = course.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .clip(RoundedCornerShape(32.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White,
                                modifier = Modifier.clickable {
                                    navController.popBackStack()
                                }
                            ) {
                                Text(
                                    text = "‹",
                                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = Color.White,
                                modifier = Modifier.clickable {
                                    isBookmarked = !isBookmarked
                                }
                            ) {
                                Text(
                                    text = if (isBookmarked) "♥" else "♡",
                                    color = if (isBookmarked) Color(0xFF14B8A6) else Color.Black,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            course.tags.take(3).forEach { tag ->
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFFE6FFFB)
                                ) {
                                    Text(
                                        text = tag,
                                        color = Color(0xFF14B8A6),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = course.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF151515)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = course.subtitle,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "⭐ ${course.rating}   •   ${course.studentsEnrolled} students   •   ${course.durationHours}h",
                            color = Color(0xFF555555),
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8FA))
                        ) {
                            Row(
                                modifier = Modifier.padding(18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = course.instructor.avatarUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(58.dp)
                                        .clip(CircleShape)
                                )

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = course.instructor.name,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = course.instructor.title,
                                        color = Color.Gray
                                    )
                                }

                                Text(
                                    text = if (isFollowing) "Following" else "Follow",
                                    color = Color(0xFF14B8A6),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        isFollowing = !isFollowing
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        Text(
                            text = "About course",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = course.description,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(26.dp))

                        Text(
                            text = "Course content",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }

                itemsIndexed(course.lessons) { index, lesson ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                            .clickable {
                                if (lesson.isFree) {
                                    navController.navigate(
                                        NavRoutes.player(safeCategoryIndex, safeCourseIndex, index)
                                    )
                                }
                            },
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8FA))
                    ) {
                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFE6FFFB)
                            ) {
                                Text(
                                    text = if (lesson.isFree) "▶" else "🔒",
                                    modifier = Modifier.padding(14.dp),
                                    color = Color(0xFF14B8A6)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = lesson.title,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "${lesson.durationMinutes} min",
                                    color = Color.Gray
                                )
                            }

                            Text(
                                text = if (lesson.isFree) "FREE" else "LOCKED",
                                color = if (lesson.isFree) Color(0xFF14B8A6) else Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            val firstFreeIndex = course.lessons.indexOfFirst { it.isFree }
                                .takeIf { it >= 0 } ?: 0

                            navController.navigate(
                                NavRoutes.player(
                                    safeCategoryIndex,
                                    safeCourseIndex,
                                    firstFreeIndex
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .height(58.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF14B8A6)
                        )
                    ) {
                        Text("Enroll now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}