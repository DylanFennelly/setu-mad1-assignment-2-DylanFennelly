package com.example.characterapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CharacterModel::class], version = 1, exportSchema = false)
abstract class CharacterDatabase: RoomDatabase() {
    abstract fun itemDao(): CharacterDao

    companion object {
        @Volatile
        private var Instance: CharacterDatabase? = null

        fun getDatabase(context: Context): CharacterDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CharacterDatabase::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}