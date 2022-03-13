package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel:ViewModel() {

    private val TAG="GameViewModel"

    private val _score = MutableLiveData(0)
    private val _currentWordCount=MutableLiveData(0)
    private val _currentScrambledWord = MutableLiveData<String>()

    val currentScrambledWord:LiveData<String> get() = _currentScrambledWord
    val score:LiveData<Int> get() = _score
    val currentWordCount:LiveData<Int> get() = _currentWordCount

    private val wordsList:MutableList<String> = mutableListOf()
    private lateinit var currentWord:String

    init {
        Log.d(TAG,"GameViewModel created")
        getWord()

    }




    private  fun getWord()
    {
        currentWord= allWordsList.random()
        val tempWord=currentWord.toCharArray()
        tempWord.shuffle()

        while(tempWord.toString().equals(currentWord,false))
            tempWord.shuffle()

        if(wordsList.contains(currentWord))
        {
            getWord()
        }
        else
        {
            _currentScrambledWord.value= String(tempWord)
            _currentWordCount.value=(_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }

    }

     fun nextWord():Boolean
    {
        if(currentWordCount.value!!< MAX_NO_OF_WORDS)
        {
            getWord()
            return true
        }

        else
            return false


    }
    private fun increaseScore()
    {
        _score.value=(_score.value)?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect(playerWord:String):Boolean
    {
        if(playerWord.equals(currentWord,true))
        {
            increaseScore()
            return true
        }
        else
        {
            return false
        }

    }

    fun reinitializeData()
    {
        _score.value=0
        _currentWordCount.value=0
        wordsList.clear()
        getWord()

    }


}