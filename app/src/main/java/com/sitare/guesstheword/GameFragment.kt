package com.sitare.guesstheword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
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

        binding.correctButton.setOnClickListener {
            viewModel.onCorrect()
            updateScoreText()
            updateWordText()
        }
        binding.skipButton.setOnClickListener {
            viewModel.onSkip()
            updateScoreText()
            updateWordText()
        }
        updateScoreText()
        updateWordText()
        return binding.root
    }

    /**
     * Called when the game is finished
     * Navigations should be made in the fragment
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score())
        NavHostFragment.findNavController(this).navigate(action)
    }

    /** Methods for updating the UI **/
    private fun updateWordText() {
        binding.wordText.text = viewModel.word()

    }

    private fun updateScoreText() {
        binding.scoreText.text = viewModel.score().toString()
    }
}