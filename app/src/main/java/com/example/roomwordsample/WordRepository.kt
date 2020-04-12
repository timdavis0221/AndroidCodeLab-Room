package com.example.roomwordsample

//import android.content.Context
import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor.
// Pass in the DAO instead of whole database, cause you only need access to the DAO
class WordRepository(private val wordDao: WordDao) {

//    private var wordDao: WordDao? = null
//    private var wordRoomDatabase: WordRoomDatabase? = null

//    constructor(wordDao: WordDao,
//                wordRoomDB: WordRoomDatabase =
//                    WordRoomDatabase.getDatabase(MainActivity.getContextInstance()!!)
//    ) {
//        this.wordDao = wordDao
//        this.wordRoomDatabase = wordRoomDB
//    }

    // Room executes all queries on a separate thread
    // Observed LiveData will notify the observer when the data has changed
    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()

    suspend fun insert(word: Word) {
        wordDao.insert(word)
//        wordRoomDatabase?.wordDao()?.insert(word)
    }
}