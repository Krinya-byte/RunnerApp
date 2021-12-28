package hu.bme.aut.runnerapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.Serializable
import java.util.*

@Entity(tableName = "rundata")
data class RunData(
    @PrimaryKey(autoGenerate = true) var runDataId: Long? = null,
    @ColumnInfo(name = "pace") var pace: Float,
    @ColumnInfo(name = "distance") var distance: Float,
    @ColumnInfo(name = "time") var time: Long,
    @ColumnInfo(name = "date") var date: Long? = DateConverter.fromDate(Calendar.getInstance().time)
) : Serializable

internal object DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}