package com.sitare.guesstheword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore:Int) : ViewModel() {
    private val _score = MutableLiveData<String>()
    val score : LiveData<String>
        get() = _score
    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain : LiveData<Boolean>
        get() = _eventPlayAgain

    init {
        _eventPlayAgain.value = false
        _score.value = finalScore.toString()
        Log.i("ScoreViewModel", "Final score: $finalScore")
    }

    fun onPlayAgainCompleted(){
        _eventPlayAgain.value = false
    }

    fun onPlayAgain(){
        _eventPlayAgain.value = true
    }
}