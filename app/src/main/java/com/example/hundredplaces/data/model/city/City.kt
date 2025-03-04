package com.example.hundredplaces.data.model.city

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity data class that represents a city
 */
@Entity(
    tableName = "cities",
    indices = [
        Index(
            value = ["name"],
            unique = true
        )
    ]
)
data class City (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String
)