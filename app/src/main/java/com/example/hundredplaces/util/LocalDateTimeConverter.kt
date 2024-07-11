package com.example.hundredplaces.util

import androidx.room.TypeConverter
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter: TypeAdapter<LocalDateTime>() {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun toString (entity: LocalDateTime): String {
        return formatter.format(entity)
    }

    @TypeConverter
    fun fromString(serialized: String): LocalDateTime {
        return LocalDateTime.parse(serialized) as LocalDateTime
    }

    override fun write(out: JsonWriter?, value: LocalDateTime?) {
        out?.value(value?.let { toString(it) })
    }

    override fun read(`in`: JsonReader?): LocalDateTime? {
        return `in`?.nextString()?.let { fromString(it) }
    }
}