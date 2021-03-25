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
package com.example.androiddevchallenge.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.model.CountDownTime
import com.example.androiddevchallenge.utils.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private var timer: CountDownTimer? = null

    private val _currentTime = MutableStateFlow(totalTime)
    val currentTime: StateFlow<Long> = _currentTime

    private var _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val _uiState = MutableStateFlow<ViewState>(ViewState.Default)

    // UI collect from this stateFlow to get the state updates
    val uiState: StateFlow<ViewState> = _uiState

    private fun startTimer() {
        timer = object : CountDownTimer(totalTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished
                _uiState.value = ViewState.Running(CountDownTime(millisUntilFinished))
            }

            override fun onFinish() {
                _uiState.value = ViewState.Finished(CountDownTime(totalTime))
                _currentTime.value = totalTime
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
            _currentTime.value = totalTime
            _uiState.value = ViewState.Finished(CountDownTime(totalTime))
            _isRunning.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
        _currentTime.value = totalTime
        _uiState.value = ViewState.Finished(CountDownTime(totalTime))
    }

    companion object {
        const val totalTime = 20 * 1000L
        const val interval = 1000L
    }
}
