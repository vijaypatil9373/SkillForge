package com.example.skillforge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun TopSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Welcome back",
                color = Color(0xFF9A9A9A),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Find your next\nskill",
                color = Color(0xFF151515),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )
        }

        Surface(
            modifier = Modifier.size(58.dp),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 3.dp
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = Color(0xFF444444)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        AsyncImage(
            model = "https://ui-avatars.com/api/?name=Aarav+Sharma&size=150&background=14B8A6&color=ffffff&bold=true&format=png",
            contentDescription = null,
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(Color(0xFF14B8A6))
        )
    }
}