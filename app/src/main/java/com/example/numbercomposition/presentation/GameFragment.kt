package com.example.numbercomposition.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.numbercomposition.R
import com.example.numbercomposition.databinding.FragmentGameBinding
import com.example.numbercomposition.domain.entity.GameResult
import com.example.numbercomposition.domain.entity.GameSettings
import com.example.numbercomposition.domain.entity.Level

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    private lateinit var level: Level

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLeftNumber.setOnClickListener {
            Toast.makeText(requireContext(), "Click", Toast.LENGTH_SHORT).show()
            launchGameFinishFragment(
                GameResult(
                true,
                5,
                5,
                GameSettings(0,0,0,0)
            )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().parcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    private fun launchGameFinishFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    companion object {
        const val NAME = "game_fragment"
        private const val KEY_LEVEL = "level"

        fun newInstance(level: Level) : GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}