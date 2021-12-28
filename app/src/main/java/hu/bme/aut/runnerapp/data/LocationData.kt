package hu.bme.aut.runnerapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class LocationData(val distance: Float = 0f, val speed: Float = 0f,val time: Long = 1)