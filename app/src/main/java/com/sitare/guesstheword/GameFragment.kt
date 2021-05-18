package com.sitare.guesstheword

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.sitare.guesstheword.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        /** Done inside the XML via data binding
        binding.correctButton.setOnClickListener {
            viewModel.onCorrect()
        }
        binding.skipButton.setOnClickListener {
            viewModel.onSkip()
        }
         */

        binding.gameViewModel = viewModel
        binding.lifecycleOwner = this

        /** DATA BINDING HANDLES THIS
        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })
         */

        /** DATA BINDING HANDLES THIS
        viewModel.word.observe(viewLifecycleOwner, Observer { newWord ->
            binding.wordText.text = newWord
        })
         */
        /** DATA BINDING HANDLES THIS
        viewModel.timeLeft.observe(viewLifecycleOwner, Observer { newTime ->
            binding.timerText.text = newTime
        })
         */

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { newEventGameFinish ->
            if (newEventGameFinish) {
                gameFinished()
                viewModel.onGameFinishComplete()
            }
        })

        // Buzzes when triggered with different buzz events
        viewModel.buzz.observe(viewLifecycleOwner, Observer { buzzType ->
            if (buzzType != GameViewModel.BuzzType.NO_BUZZ) {
                buzz(buzzType.pattern)
                viewModel.onBuzzComplete()
            }
        })
        return binding.root
    }

    /**
     * Called when the game is finished
     * Navigations should be made in the fragment
     */
    private fun gameFinished() {
        //elvis operator: if null, pass 0
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0)
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
                Log.i("Buzz", "if statement")
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
                Log.i("Buzz", "else statement")
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE))
                Log.i("Buzzer", "if working")
            } else {
                Log.i("Buzzer", "else working")
            }
        }
    }

}