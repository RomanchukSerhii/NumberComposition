package com.example.numbercomposition.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.numbercomposition.databinding.FragmentGameBinding
import com.example.numbercomposition.domain.entity.GameResult

class GameFragment : Fragment() {
    private val args by navArgs<GameFragmentArgs>()
    private val viewModelFactory by lazy {
        GameViewModelFactory(requireActivity().application, args.level)
    }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            with(binding) {
                add(tvOption1)
                add(tvOption2)
                add(tvOption3)
                add(tvOption4)
                add(tvOption5)
                add(tvOption6)
            }
        }
    }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setClickListenersToOptions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setClickListenersToOptions() {
        tvOptions.forEach { tvOption ->
            tvOption.setOnClickListener {
                val answer = tvOption.text.toString().toInt()
                viewModel.chooseAnswer(answer)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.apply {

            formattedTimer.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it
            }

            question.observe(viewLifecycleOwner) { question ->
                with(binding) {
                    tvSum.text = question.sum.toString()
                    tvLeftNumber.text = question.visibleNumber.toString()
                }
                for (i in tvOptions.indices) {
                    tvOptions[i].text = question.options[i].toString()
                }
            }

            progressAnswers.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.text = it
            }

            percentOfRightAnswers.observe(viewLifecycleOwner) {
                binding.progressBar.setProgress(it, true)
            }

            enoughCount.observe(viewLifecycleOwner) {
                val color = getColorByState(it)
                binding.tvAnswersProgress.setTextColor(color)
            }

            enoughPercent.observe(viewLifecycleOwner) {
                val color = getColorByState(it)
                binding.progressBar.progressTintList = ColorStateList.valueOf(color)
            }

            minPercent.observe(viewLifecycleOwner) {
                binding.progressBar.secondaryProgress = it
            }

            gameResult.observe(viewLifecycleOwner) {
                launchGameFinishFragment(it)
            }
        }
    }

    private fun getColorByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    private fun launchGameFinishFragment(gameResult: GameResult) {
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishFragment(gameResult)
        )
    }
}