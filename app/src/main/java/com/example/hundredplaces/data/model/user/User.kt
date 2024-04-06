package com.example.hundredplaces.data.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "users",
    indices = [
        Index(
            value = ["email"],
            unique = true
        )
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private val id: Int = 0,
    @ColumnInfo(name = "name")
    private val name: String,
    @ColumnInfo(name = "email")
    private val email: String,
    @ColumnInfo(name = "password")
    private val password: String
)
