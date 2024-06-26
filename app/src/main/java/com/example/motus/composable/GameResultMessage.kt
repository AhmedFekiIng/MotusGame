package com.example.motus.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameResultMessage(message: String, color: Color = Color.Green, modifier: Modifier = Modifier) {
    Text(
        text = message,
        color = color,
        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
        modifier = modifier.padding(16.dp)
    )
}