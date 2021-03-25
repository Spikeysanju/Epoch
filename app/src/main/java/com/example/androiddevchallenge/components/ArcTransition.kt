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

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

class ArcTransition(
    progress: State<Float>,
) {
    val progress by progress
}

@Composable
fun updateCircularTransitionData(
    remainingTime: Long,
    totalTime: Long
): ArcTransition {
    val transition = updateTransition(targetState = remainingTime)

    val progress = transition.animateFloat(
        transitionSpec = { tween(1000, easing = FastOutSlowInEasing) }
    ) { timeLeft ->
        if (timeLeft < 0) {
            360f
        } else {
            360f - ((360f / totalTime) * (totalTime - timeLeft))
        }
    }

    return remember(transition) { ArcTransition(progress = progress) }
}
