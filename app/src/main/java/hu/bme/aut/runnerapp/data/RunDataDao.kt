package hu.bme.aut.runnerapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.io.Serializable

@Dao
interface RunDataDao {

    @Query("SELECT * FROM rundata")
    fun getAllRunData(): List<RunData>

    @Insert
    fun addRunData(locData: RunData): Long

    @Delete
    fun deleteRunData(locData: RunData)

    @Update
    fun updateRunData(locData: RunData)

}
