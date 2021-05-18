package com.sitare.guesstheword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        viewModel.timeLeft.observe(viewLifecycleOwner, Observer { newTime ->
            binding.timerText.text = newTime
        })

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { newEventGameFinish ->
            if (newEventGameFinish) {
                gameFinished()
                viewModel.onGameFinishComplete()
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
}