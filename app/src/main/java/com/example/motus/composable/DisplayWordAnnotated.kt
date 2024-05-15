package com.example.motus.composable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.motus.GameState

@Composable
fun DisplayWordAnnotated(state: GameState, modifier: Modifier = Modifier) {
    val displayWord = buildAnnotatedString {
        state.displayWordWithoutOrder.forEachIndexed { index, char ->
            val color = when {
                state.correctIndices.contains(index) -> Color.Red
                state.misplacedIndices.contains(index) -> Color.Yellow
                else -> Color.Black
            }
            withStyle(style = SpanStyle(color = color, fontWeight = FontWeight.Bold)) {
                append(char)
            }
            if (index != state.displayWordWithoutOrder.length - 1) {
                append(' ')
            }
        }
    }

    Text(
        text = displayWord,
        style = TextStyle(fontSize = 24.sp),
        modifier = modifier
    )
}
