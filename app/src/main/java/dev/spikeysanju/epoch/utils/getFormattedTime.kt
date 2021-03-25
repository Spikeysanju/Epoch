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
package com.example.epoch.utils

import java.util.concurrent.TimeUnit

fun getFormattedTime(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) -
        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))

    val minutesFormatted = if (minutes.toString().length == 1) "0$minutes" else minutes.toString()
    val secondsFormatted = if (seconds.toString().length == 1) "0$seconds" else seconds.toString()

    return "$minutesFormatted : $secondsFormatted"
}
