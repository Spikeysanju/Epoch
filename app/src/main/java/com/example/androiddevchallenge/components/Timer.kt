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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.theme.blue200
import com.example.androiddevchallenge.ui.theme.blue500
import com.example.androiddevchallenge.ui.theme.blueBG
import com.example.androiddevchallenge.ui.theme.blueText
import com.example.androiddevchallenge.ui.theme.card
import com.example.androiddevchallenge.ui.theme.deepGold
import com.example.androiddevchallenge.utils.Constants
import com.example.androiddevchallenge.utils.getFormattedTime
import com.example.androiddevchallenge.viewmodel.MainViewModel
import kotlin.math.roundToInt

@Composable
fun TimerScreen(viewModel: MainViewModel, toggleTheme: () -> Unit) {

    val currentTime = viewModel.currentTime.collectAsState().value
    val isRunning = viewModel.isRunning.collectAsState().value
    val transitionData = updateCircularTransitionData(
        remainingTime = currentTime,
        totalTime = MainViewModel.totalTime
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = blueBG),
        contentAlignment = Alignment.Center
    ) {

        Kabel(viewModel, isRunning)

        CountDownView(transition = transitionData)

        Toolbar(toggleTheme)

        CountDownTimerText(remainingTime = currentTime)
    }
}

@Composable
fun CountDownView(transition: ArcTransition) {
    Canvas(
        modifier = Modifier
            .wrapContentSize()
            .background(color = blueBG)
    ) {

        inset(size.width / 2 - Constants.TIMER_RADIUS, size.height / 2 - Constants.TIMER_RADIUS) {

            val gradientBrush = Brush.linearGradient(listOf(blue500, blue200))
            drawArc(
                brush = gradientBrush,
                startAngle = 270f,
                sweepAngle = transition.progress,
                useCenter = false,
                style = Stroke(width = 100f, cap = StrokeCap.Round),
                blendMode = BlendMode.SrcIn
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
fun CountDownTimerText(remainingTime: Long) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TimeRemaining(
            timeRemaining = remainingTime,
        )
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

@Composable
fun Kabel(viewModel: MainViewModel, isRunning: Boolean) {

    // actual state for icon color
    var iconColor by remember { mutableStateOf(Color.Gray) }

    // actual composable state for offsetY axis
    var offsetY by remember { mutableStateOf(1600f) }

    iconColor = if (offsetY <= 1300) {
        deepGold
    } else {
        Color.Gray
    }

    // composable state for cable wire height
    val animatedHeightState = animateDpAsState(
        targetValue = if (offsetY <= 1300) offsetY.dp else offsetY.dp,
        animationSpec = tween(300, easing = LinearEasing)
    )
    // composable state for cable color
    val animatedColor = animateColorAsState(
        targetValue = iconColor, animationSpec = tween(300, easing = LinearEasing)
    )

    Box(
        modifier = Modifier
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState(
                    onDelta = {
                        offsetY += it
                    }
                ),
                onDragStopped = {
//                    offsetY = if (offsetY <= 1300) 1300f else 1600f

                    if (offsetY <= 1300) {
                        offsetY = 1300f
                        if (isRunning) {
                            viewModel.onResetClicked()
                        } else {
                            viewModel.onStartClicked()
                        }
                    } else {
                        offsetY = 1600f
                        viewModel.onResetClicked()
                    }
                }
            )
            .fillMaxSize()
            .animateContentSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Box(
            Modifier
                .size(20.dp, animatedHeightState.value)
                .background(Color.White),
        ) {

            Box(
                Modifier
                    .requiredSize(100.dp, 100.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    val icon = painterResource(id = R.drawable.ic_lightning)
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp, 24.dp),
                        tint = animatedColor.value
                    )
                }
            }
        }
    }
}
