

package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {

    private val TAG="GameFragment"



    private val viewModel:GameViewModel by viewModels()


    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(inflater,R.layout.game_fragment, container, false)
        Log.d(TAG,"GameFragment Created/Re-Created")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.gameViewModel=viewModel
        binding.maxNofWords= MAX_NO_OF_WORDS
        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
        binding.lifecycleOwner=viewLifecycleOwner

        // Update the UI

        binding.score.text = getString(R.string.score, 0)
        binding.wordCount.text = getString(
                R.string.word_count, 0, MAX_NO_OF_WORDS)




    }

    private fun showFinalScoreDialog(){

        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.score,viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)){_,_ ->
                exitGame()

            }
            .setPositiveButton(getString(R.string.play_again)){_,_ ->
                restartGame()
            }
            .show()

    }



    private fun onSubmitWord() {

        val playerWord=binding.textInputEditText.text.toString()


        if(viewModel.isUserWordCorrect(playerWord))
        {
            setErrorTextField(false)

            if(!viewModel.nextWord())
            {
                showFinalScoreDialog()

            }


        }

        else
        {
            setErrorTextField(true)
        }




    }

    /*
     * Skips the current word without changing the score.
     * Increases the word count.
     */
    private fun onSkipWord() {
        if(viewModel.nextWord())
        {

            setErrorTextField(false)

        }
        else
        {
            showFinalScoreDialog()
        }
    }


//


    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)

    }


    private fun exitGame() {
       activity?.finishAffinity()
        return
    }


    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }



}
