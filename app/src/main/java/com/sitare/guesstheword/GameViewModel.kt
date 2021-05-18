package com.sitare.guesstheword

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel : ViewModel() {
    companion object {
        // These represent different important times
        const val DONE = 100L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 20000L
        // This is the time when the phone will start buzzing each second
        private const val COUNTDOWN_PANIC_SECONDS = 10L
    }

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    private val timer: CountDownTimer

    private val _buzz = MutableLiveData<BuzzType>()
    val buzz : LiveData<BuzzType>
        get() = _buzz

    private val _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word

    private val _timeLeft = MutableLiveData<Long>()
    val timeLeft : LiveData<Long>
        get() = _timeLeft

    val timeLeftString = Transformations.map(timeLeft) { time ->
        DateUtils.formatElapsedTime(time)
    }

    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
            get() = _score

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish : LiveData<Boolean>
            get() = _eventGameFinish

    private lateinit var wordList: MutableList<String>

    init {
        _eventGameFinish.value = false
        _score.value = 0
        Log.i("GameViewModel", "GameViewModel created")
        resetList()
        nextWord()

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
               _timeLeft.value = millisUntilFinished / ONE_SECOND
                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    _buzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _timeLeft.value = DONE
                _eventGameFinish.value = true
                _buzz.value = BuzzType.GAME_OVER
            }
        }

        timer.start()

    }

    override fun onCleared() {
        super.onCleared()
        //Log.i("GameViewModel", "GameViewModel destroyed")
        timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            //_eventGameFinish.value = true
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        //null check minus operation
        _score.value = (_score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        //null safety operation
        _score.value = (_score.value)?.plus(1)
        _buzz.value = BuzzType.CORRECT
        nextWord()
    }

    fun onGameFinishComplete(){
        _eventGameFinish.value = false
    }

    fun onBuzzComplete(){
        _buzz.value = BuzzType.NO_BUZZ
    }


}