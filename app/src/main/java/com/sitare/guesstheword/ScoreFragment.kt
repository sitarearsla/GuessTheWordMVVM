package com.sitare.guesstheword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sitare.guesstheword.databinding.FragmentScoreBinding

class ScoreFragment : Fragment() {
    private lateinit var binding: FragmentScoreBinding

    // val viewmodel: ScoreViewModel by viewModels { ScoreViewModelFactory(score)} //With factory
    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_score,
            container,
            false)
        val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()
        val score = scoreFragmentArgs.score
        viewModelFactory = ScoreViewModelFactory(score)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)

        binding.scoreViewModel = viewModel
        binding.lifecycleOwner = this
        binding.scoreText.text = score.toString()
        /**
        ****** DATA BINDING HANDLES THIS ********
        binding.playAgainButton.setOnClickListener {
            viewModel.onPlayAgain()
        }
         */

        /** DATA BINDING HANDLES THIS
        viewModel.score.observe(viewLifecycleOwner, { newScore ->
            binding.scoreText.text = newScore
        })
        */

        viewModel.eventPlayAgain.observe(viewLifecycleOwner, { playAgain ->
           if (playAgain) {
               onPlayAgain()
               viewModel.onPlayAgainCompleted()
           }
        })
        return binding.root
    }

    private fun onPlayAgain() {
        findNavController().navigate(ScoreFragmentDirections.actionRestart())
    }
}