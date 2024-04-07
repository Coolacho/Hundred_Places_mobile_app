package com.example.hundredplaces.util

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun toString (entity: LocalDateTime): String {
        return formatter.format(entity)
    }

    @TypeConverter
    fun fromString(serialized: String): LocalDateTime {
        return formatter.parse(serialized) as LocalDateTime
    }
}