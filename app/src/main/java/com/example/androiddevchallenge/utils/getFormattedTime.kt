package com.example.androiddevchallenge.utils

import java.util.concurrent.TimeUnit

fun getFormattedTime(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) -
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))

    val minutesFormatted = if (minutes.toString().length == 1) "0$minutes" else minutes.toString()
    val secondsFormatted = if (seconds.toString().length == 1) "0$seconds" else seconds.toString()

    return "$minutesFormatted : $secondsFormatted"
}