/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.blue500
import com.example.androiddevchallenge.ui.theme.blueBG
import com.example.androiddevchallenge.ui.theme.blueText
import com.example.androiddevchallenge.ui.theme.card
import com.example.androiddevchallenge.utils.Constants
import com.example.androiddevchallenge.utils.getFormattedTime
import com.example.androiddevchallenge.viewmodel.MainViewModel


@Composable
fun TimerScreen(viewModel: MainViewModel, toggleTheme: () -> Unit) {

    val currentTime = viewModel.currentTime.collectAsState().value
    val isRunning = viewModel.isRunning.collectAsState().value

    val transitionData = updateCircularTransitionData(
        remainingTime = currentTime,
        totalTime = MainViewModel.totalTime
    )

    Box(modifier = Modifier.fillMaxSize()) {

        CountDownView(transition = transitionData)

        Toolbar(toggleTheme)

        CenterTimerText(currentTime)

        FloatingButton(viewModel = viewModel, isRunning = isRunning)
    }

}

@Composable
fun CountDownView(transition: ArcTransition) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(color = blueBG)
    ) {

        inset(size.width / 2 - Constants.TIMER_RADIUS, size.height / 2 - Constants.TIMER_RADIUS) {

            drawArc(
                startAngle = 270f,
                sweepAngle = transition.progress,
                useCenter = false,
                color = transition.color,
                style = Stroke(width = 100f, cap = StrokeCap.Round)
            )

            drawCircle(
                color = card,
                radius = Constants.TIMER_RADIUS,
                center = center,
            )
        }
    }

}

@Composable
fun Toolbar(toggleTheme: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        TopBar(
            onToggle = {
                toggleTheme()
            }
        )
    }
}

@Composable
fun CenterTimerText(remainingTime: Long) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TimeRemaining(
            timeRemaining = remainingTime
        )
    }
}

@Composable
fun FloatingButton(isRunning: Boolean, viewModel: MainViewModel) {
    val startIcon = Icons.Filled.PlayArrow
    val stopIcon = Icons.Filled.Stop
    var buttonColor by remember { mutableStateOf(blue500) }
    var buttonIcon by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {

                if (isRunning) {
                    viewModel.onResetClicked()
                    buttonIcon = false
                    buttonColor = blue500
                } else {
                    viewModel.onStartClicked()
                    buttonIcon = true
                    buttonColor = Color.Red
                }
            },
            backgroundColor = buttonColor, contentColor = Color.White
        ) {

            val icon = if (buttonIcon) {
                rememberVectorPainter(image = stopIcon)
            } else {
                rememberVectorPainter(image = startIcon)
            }

            Icon(painter = icon, contentDescription = "Fab button")
        }
    }
}

@Composable
fun TimeRemaining(timeRemaining: Long) {
    val formattedTime = getFormattedTime(millis = timeRemaining)
    Text(
        text = formattedTime,
        color = blueText,
        style = MaterialTheme.typography.h4
    )
}
