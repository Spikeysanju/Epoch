/*
 * Copyright 2021 Spikey Sanju
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.spikeysanju.epoch.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.epoch.components.Toolbar
import com.example.epoch.components.updateCircularTransitionData
import com.example.epoch.ui.theme.blue200
import com.example.epoch.ui.theme.blue400
import com.example.epoch.ui.theme.blue500
import com.example.epoch.ui.theme.blueBG
import com.example.epoch.ui.theme.card
import com.example.epoch.ui.theme.deepGold
import com.example.epoch.ui.theme.pinkText
import com.example.epoch.utils.Constants
import com.example.epoch.utils.getFormattedTime
import com.example.epoch.viewmodel.MainViewModel
import dev.spikeysanju.epoch.R
import kotlin.math.roundToInt

@Composable
fun TimerScreen(viewModel: MainViewModel) {

    var progress by remember { mutableStateOf(0f) }
    val currentTime = viewModel.currentTime.collectAsState().value
    val isRunning = viewModel.isRunning.collectAsState().value
    val transitionData = updateCircularTransitionData(
        remainingTime = currentTime,
        totalTime = MainViewModel.totalTime
    )

    /**
     * animation state for On and Off arc
     * @param transitionData
     */

    val default = animateFloatAsState(
        targetValue = if (isRunning) transitionData.progress else 0f,
        animationSpec = tween(300, easing = LinearEasing)
    )
    progress = default.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = blueBG),
        contentAlignment = Alignment.Center
    ) {

        CableView(viewModel, isRunning)

        CountDownView(progress)

        Toolbar()

        CountDownTimerText(remainingTime = currentTime)
    }
}

/**
 * CountDownView which contains Canvas inside with DrawArc (Counter progress)
 * & DrawCircle (Counter BG Frame)
 * @param progress
 */

@Composable
fun CountDownView(progress: Float) {
    Canvas(
        modifier = Modifier
            .wrapContentSize()
            .background(color = blueBG)
    ) {

        inset(size.width / 2 - Constants.TIMER_RADIUS, size.height / 2 - Constants.TIMER_RADIUS) {

            val gradientBrush = Brush.linearGradient(listOf(blue500, blue200, blue400))
            drawArc(
                brush = gradientBrush,
                startAngle = 270f,
                sweepAngle = progress,
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

/**
 * CountDownTimerText which contains Timer text which allows us to show progress time
 * @param remainingTime
 */
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
        color = pinkText,
        style = MaterialTheme.typography.h4
    )
}

/**
 * Pin which creates Charger like pin view
 */
@Composable
fun PinView() {
    Box(
        Modifier
            .requiredSize(60.dp)
            .clip(RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp))
            .background(Color.Gray)
    )
}

/**
 * CableView contains three Box layout which stacked up on each other view.
 * Box 1 is the root layout which acts as an draggable view.
 * Box 2 is cable with dynamic height. It means whenever we drag the root Box 1 layout height
 * of this layout be calculated calculated accordingly
 * Box 3 which contains flash icon inside. Color of this flash icon is handled according to the offset.
 * @param viewModel
 * @param isRunning
 */
@Composable
fun CableView(viewModel: MainViewModel, isRunning: Boolean) {

    /**
     * states for icon color, run check & cable wire offset
     */
    var iconColor by remember { mutableStateOf(Color.Gray) }
    val isRunningNow by remember { mutableStateOf(isRunning) }
    var offsetY by remember { mutableStateOf(1600f) }

    iconColor = if (offsetY <= 1200) {
        deepGold
    } else {
        Color.Gray
    }

    /**
     * composable state for cable wire height
     */
    val animatedHeightState = animateDpAsState(
        targetValue = if (offsetY <= 1200) offsetY.dp else offsetY.dp,
        animationSpec = tween(300, easing = LinearEasing)
    )

    /**
     * composable state for cable color
     */
    val animatedColor = animateColorAsState(
        targetValue = iconColor,
        animationSpec = tween(300, easing = LinearEasing)
    )

    // Box1 - Root Layout
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
                    if (offsetY <= 1200) {
                        offsetY = 1200f
                        if (isRunningNow) {
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

        // Box2 - Cable wire layout
        Box(
            Modifier
                .size(20.dp, animatedHeightState.value)
                .background(card),
        ) {

            // Cable pin view
            PinView()

            // Box3 - Cable head layout
            Box(
                Modifier
                    .requiredSize(100.dp, 100.dp)
                    .clip(RoundedCornerShape(4.dp, 4.dp, 4.dp, 4.dp))
                    .padding(0.dp, 20.dp, 0.dp, 0.dp)
                    .background(card),
                contentAlignment = Alignment.Center
            ) {

                IconButton(onClick = { }) {
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
