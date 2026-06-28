package com.example.skillforge.ui.player

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
fun PlayerScreen(
    navController: NavController,
    categoryIndex: Int,
    courseIndex: Int,
    lessonIndex: Int,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0.38f) }
    var isCompleted by remember { mutableStateOf(false) }

    when {
        state.isLoading -> LoadingView()
        state.error != null -> ErrorView(state.error ?: "Error")

        else -> {
            val safeCategoryIndex = categoryIndex.coerceIn(0, state.categories.lastIndex)
            val safeCourseIndex =
                courseIndex.coerceIn(0, state.categories[safeCategoryIndex].courses.lastIndex)

            val course = state.categories[safeCategoryIndex].courses[safeCourseIndex]
            val lessons = course.lessons
            val safeLessonIndex = lessonIndex.coerceIn(0, lessons.lastIndex)
            val lesson = lessons[safeLessonIndex]

            val previousIndex = safeLessonIndex - 1
            val nextIndex = safeLessonIndex + 1

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                item {
                    Box {
                        AsyncImage(
                            model = course.thumbnailUrl,
                            contentDescription = lesson.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(310.dp),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(310.dp)
                                .background(Color.Black.copy(alpha = 0.45f))
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.45f),
                                modifier = Modifier.clickable {
                                    navController.popBackStack()
                                }
                            ) {
                                Text(
                                    text = "‹",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.45f)
                            ) {
                                Text(
                                    text = "⛶",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clickable { isPlaying = !isPlaying }
                        ) {
                            Text(
                                text = if (isPlaying) "Ⅱ" else "▶",
                                color = Color.Black,
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.padding(28.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(horizontal = 22.dp, vertical = 18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${(progress * lesson.durationMinutes).toInt()}:00",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )

                            Slider(
                                value = progress,
                                onValueChange = { progress = it },
                                modifier = Modifier.weight(1f),
                                colors = SliderDefaults.colors(
                                    thumbColor = Color.White,
                                    activeTrackColor = Color(0xFF14B8A6),
                                    inactiveTrackColor = Color.White.copy(alpha = 0.35f)
                                )
                            )

                            Text(
                                text = "${lesson.durationMinutes}:00",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "LESSON ${safeLessonIndex + 1} · ${course.title.uppercase()}",
                            color = Color(0xFF14B8A6),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${lesson.durationMinutes} min · ${if (lesson.isFree) "Free lesson" else "Locked lesson"}",
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = {
                                    if (previousIndex >= 0) {
                                        navController.navigate(
                                            NavRoutes.player(
                                                safeCategoryIndex,
                                                safeCourseIndex,
                                                previousIndex
                                            )
                                        )
                                    }
                                },
                                enabled = previousIndex >= 0,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Previous")
                            }

                            Button(
                                onClick = {
                                    if (nextIndex < lessons.size && lessons[nextIndex].isFree) {
                                        navController.navigate(
                                            NavRoutes.player(
                                                safeCategoryIndex,
                                                safeCourseIndex,
                                                nextIndex
                                            )
                                        )
                                    }
                                },
                                enabled = nextIndex < lessons.size && lessons[nextIndex].isFree,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF14B8A6)
                                )
                            ) {
                                Text("Next")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { isCompleted = !isCompleted },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCompleted) Color(0xFF0F766E) else Color(0xFF14B8A6)
                            )
                        ) {
                            Text(
                                text = if (isCompleted) "Completed ✓" else "Mark as Complete",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                            Text(
                                text = "Lessons",
                                color = if (selectedTab == 0) Color.Black else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { selectedTab = 0 }
                            )

                            Text(
                                text = "Notes",
                                color = if (selectedTab == 1) Color.Black else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { selectedTab = 1 }
                            )

                            Text(
                                text = "Resources",
                                color = if (selectedTab == 2) Color.Black else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { selectedTab = 2 }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .width(
                                    when (selectedTab) {
                                        0 -> 78.dp
                                        1 -> 58.dp
                                        else -> 88.dp
                                    }
                                )
                                .height(4.dp)
                                .background(Color(0xFF14B8A6), RoundedCornerShape(20.dp))
                        )

                        Spacer(modifier = Modifier.height(22.dp))
                    }
                }

                when (selectedTab) {
                    0 -> {
                        itemsIndexed(lessons) { index, item ->
                            val isCurrent = index == safeLessonIndex

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 8.dp)
                                    .clickable {
                                        if (item.isFree) {
                                            navController.navigate(
                                                NavRoutes.player(
                                                    safeCategoryIndex,
                                                    safeCourseIndex,
                                                    index
                                                )
                                            )
                                        }
                                    },
                                shape = RoundedCornerShape(22.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isCurrent) Color(0xFFE6FFFB) else Color(0xFFF7F8FA)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(18.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isCurrent) Color(0xFF14B8A6) else Color(0xFFE6FFFB)
                                    ) {
                                        Text(
                                            text = when {
                                                isCurrent -> if (isPlaying) "Ⅱ" else "▶"
                                                item.isFree -> "▶"
                                                else -> "🔒"
                                            },
                                            color = if (isCurrent) Color.White else Color(0xFF14B8A6),
                                            modifier = Modifier.padding(15.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.title,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCurrent) Color(0xFF0F766E) else Color.Black
                                        )

                                        Text(
                                            text = if (isCurrent)
                                                "Now playing · ${item.durationMinutes} min"
                                            else
                                                "${item.durationMinutes} min",
                                            color = Color.Gray
                                        )
                                    }

                                    Text(
                                        text = if (item.isFree) "FREE" else "LOCKED",
                                        color = if (item.isFree) Color(0xFF14B8A6) else Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    1 -> {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                shape = RoundedCornerShape(22.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8FA))
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = "Lesson Notes",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = lesson.content,
                                        color = Color.Gray
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "Tip: Revise this lesson before moving to the next module.",
                                        color = Color(0xFF14B8A6),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    2 -> {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                shape = RoundedCornerShape(22.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8FA))
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = "Resources",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text("• Video URL: ${lesson.videoUrl}", color = Color.Gray)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("• Course language: ${course.language}", color = Color.Gray)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("• Last updated: ${course.lastUpdated}", color = Color.Gray)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("• Instructor: ${course.instructor.name}", color = Color.Gray)
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(34.dp))
                }
            }
        }
    }
}