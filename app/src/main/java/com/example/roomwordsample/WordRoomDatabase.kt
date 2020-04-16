package com.example.roomwordsample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Database migrations are beyond the scope of this codelab,
 * so we set exportSchema to false here to avoid a build warning.
 * In a real app, you should consider setting a directory for Room to use to export the schema
 * so you can check the current schema into your version control system.
 */
@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    // The database exposes DAOs through an abstract "getter" method for each @Dao.
    abstract fun wordDao(): WordDao

    companion object {
        // singleton prevents having multiple instances of the database opened at the same time.
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WordRoomDatabase {
//            val tempInstance = INSTANCE
//            /*if (tempInstance != null) {
//                return tempInstance
//            }*/
//            tempInstance?.apply {
//                return this
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    WordRoomDatabase::class.java,
//                    "word_table"
//                ).addCallback(WordDatabaseCallback(scope)).build()
//                INSTANCE = instance
//                return instance
//            }

            // refactor :
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_table"
                ).addCallback(WordDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // If you only want to populate the database the first time the app is launched
            // populate the db here :)
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let {database ->
                scope.launch {
                    populateDatabas(database.wordDao())
                }
            }
        }

        suspend fun populateDatabas(wordDao: WordDao) {
            // Delete all contents here
            wordDao.deleteAll()

            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("Room !")
            wordDao.insert(word)
        }
    }
}