package hu.bme.aut.runnerapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RunData::class], version = 4)
abstract class RunDatabase : RoomDatabase() {

        abstract fun runDataDao(): RunDataDao

        companion object {
            fun getDatabase(context: Context): RunDatabase {
                return Room.databaseBuilder(
                    context.applicationContext,
                    RunDatabase::class.java, "rundata_database"
                ).fallbackToDestructiveMigration()
                    .build()
            }
        }
    }