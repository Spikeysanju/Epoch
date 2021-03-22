package com.example.androiddevchallenge.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private var timer: CountDownTimer? = null

    private val _remainingTime = MutableStateFlow(totalTime)
    val remainingTime: StateFlow<Long> = _remainingTime

    private var _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private fun startTimer() {
        timer = object : CountDownTimer(totalTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                _remainingTime.value = millisUntilFinished
            }

            override fun onFinish() {
                _remainingTime.value = totalTime
                _isRunning.value = false
            }

        }
    }

    fun onStartClicked() {
        startTimer()
        timer?.start()
        _isRunning.value = true
    }

    fun onResetClicked() {
        if (_isRunning.value) {
            timer?.cancel()
            _remainingTime.value = totalTime
            _isRunning.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
        _remainingTime.value = totalTime
    }


    companion object {
        const val totalTime = 60 * 1000L // 60 seconds timer, you can change it
        const val interval = 1 * 1000L
    }
}