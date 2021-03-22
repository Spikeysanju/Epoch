package com.example.androiddevchallenge.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.purple200
import com.example.androiddevchallenge.ui.theme.purple500

const val CIRCULAR_TIMER_RADIUS = 300f

@Composable
fun Timer() {

    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(CIRCULAR_TIMER_RADIUS.dp)
    ) {
        inset(size.width / 2 - CIRCULAR_TIMER_RADIUS, size.height / 2 - CIRCULAR_TIMER_RADIUS) {

            drawCircle(
                color = purple200,
                radius = CIRCULAR_TIMER_RADIUS,
                center = center,
                style = Stroke(width = 70f, cap = StrokeCap.Round)
            )

            drawArc(
                startAngle = 270f,
                sweepAngle = 90f,
                useCenter = false,
                color = purple500,
                style = Stroke(width = 70f, cap = StrokeCap.Round)
            )

        }

    }
}

