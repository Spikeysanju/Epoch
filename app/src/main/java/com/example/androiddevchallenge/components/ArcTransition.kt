package com.example.androiddevchallenge.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.androiddevchallenge.ui.theme.blue500
import com.example.androiddevchallenge.ui.theme.pinkText

class ArcTransition(
    progress: State<Float>,
    color: State<Color>
) {
    val progress by progress
    val color by color
}

@Composable
fun updateCircularTransitionData(
    remainingTime: Long,
    totalTime: Long
): ArcTransition {
    val transition = updateTransition(targetState = remainingTime)

    val progress = transition.animateFloat(
        transitionSpec = { tween(1500, easing = LinearEasing) }
    ) { remTime ->
        if (remTime < 0) {
            360f
        } else {
            360f - ((360f / totalTime) * (totalTime - remTime))
        }
    }

    val color = transition.animateColor(
        transitionSpec = {
            tween(800, easing = LinearEasing)
        }
    ) {
        if (progress.value < 180f && progress.value > 90f) {
            blue500
        } else if (progress.value <= 90f) {
            pinkText
        } else {
            blue500
        }
    }

    return remember(transition) { ArcTransition(progress = progress, color = color) }
}