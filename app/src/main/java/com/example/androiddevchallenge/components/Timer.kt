package com.example.androiddevchallenge.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.blue500
import com.example.androiddevchallenge.ui.theme.blueBG
import com.example.androiddevchallenge.ui.theme.blueText
import com.example.androiddevchallenge.ui.theme.card
import com.example.androiddevchallenge.utils.getFormattedTime
import com.example.androiddevchallenge.viewmodel.MainViewModel

const val CIRCULAR_TIMER_RADIUS = 300f
const val TOTAL_TIME = MainViewModel.totalTime

@Composable
fun Timer(mainViewModel: MainViewModel) {

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(color = blueBG)
    ) {

        inset(size.width / 2 - CIRCULAR_TIMER_RADIUS, size.height / 2 - CIRCULAR_TIMER_RADIUS) {

            drawArc(
                startAngle = 270f,
                sweepAngle = 90f,
                useCenter = false,
                color = blue500,
                style = Stroke(width = 100f, cap = StrokeCap.Round)
            )

            drawCircle(
                color = card,
                radius = CIRCULAR_TIMER_RADIUS,
                center = center,
            )

        }

    }


    CenterTimerText()

    FloatingButton(mainViewModel = mainViewModel)

}


@Composable
fun CenterTimerText() {
    Box(
        modifier = Modifier
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        TimeRemaining(
            mainViewModel = MainViewModel(),
            modifier = Modifier.wrapContentSize(),
            timeRemaining = TOTAL_TIME
        )
    }
}

@Composable
fun FloatingButton(mainViewModel: MainViewModel) {
    var buttonColor by remember { mutableStateOf(blue500) }
    var buttonIcon by remember { mutableStateOf(Icons.Filled.PlayArrow) }

    val startIcon = Icons.Filled.PlayArrow
    val stopIcon = Icons.Filled.Stop


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp), contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(onClick = {

            mainViewModel.onStartClicked()
            buttonColor = when (buttonColor) {
                blue500 -> Color.Red
                Color.Red -> blue500
                else -> Color.Green
            }

        }, backgroundColor = buttonColor, contentColor = Color.White) {

            buttonIcon = if (buttonIcon == startIcon) stopIcon else startIcon
            Icon(buttonIcon, "")
        }
    }
}

@Composable
fun TimeRemaining(mainViewModel: MainViewModel, modifier: Modifier, timeRemaining: Long) {
    val formattedTime = getFormattedTime(millis = mainViewModel.remainingTime.value)
    Text(
        text = formattedTime,
        color = blueText,
        style = MaterialTheme.typography.h4,
        modifier = modifier
    )
}

