package com.example.hundredplaces.util

import androidx.room.TypeConverter
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.Instant
import java.time.format.DateTimeFormatter

class InstantConverter: TypeAdapter<Instant>() {

    private val formatter = DateTimeFormatter.ISO_INSTANT

    @TypeConverter
    fun toString (entity: Instant): String {
        return formatter.format(entity)
    }

    @TypeConverter
    fun fromString(serialized: String): Instant {
        return Instant.parse(serialized) as Instant
    }

    override fun write(out: JsonWriter?, value: Instant?) {
        out?.value(value?.let { toString(it) })
    }

    override fun read(`in`: JsonReader?): Instant? {
        return `in`?.nextString()?.let { fromString(it) }
    }
}