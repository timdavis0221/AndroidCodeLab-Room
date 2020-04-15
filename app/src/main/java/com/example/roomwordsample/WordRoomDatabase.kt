package com.example.roomwordsample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

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

        fun getDatabase(context: Context): WordRoomDatabase {
            val tempInstance = INSTANCE
            /*if (tempInstance != null) {
                return tempInstance
            }*/
            tempInstance?.apply {
                return this
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_table"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}