package com.chronelab.roomdatabase.database

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverter {

    @TypeConverter
    fun fromTimestamp(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}
