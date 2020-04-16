package com.example.roomwordsample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val wordDao : WordDao by lazy {
        WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
    }
    private val repository : WordRepository by lazy { WordRepository(wordDao) }

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits :
    // 1. We can put an observer on the data (instead of polling for changes) and only update
    // the UI when the data actually changes.
    // 2. Repository is completely separated from the UI through the ViewModel
    val allWords: LiveData<List<Word>> by lazy { repository.allWords }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }

    fun insertTest(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(word)
    }
}