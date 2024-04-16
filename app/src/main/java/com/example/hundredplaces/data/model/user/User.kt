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
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "museums_visited")
    val museumsVisited: Int = 0,
    @ColumnInfo(name = "peaks_visited")
    val peaksVisited: Int = 0,
    @ColumnInfo(name = "galleries_visited")
    val galleriesVisited: Int = 0,
    @ColumnInfo(name = "caves_visited")
    val cavesVisited: Int = 0,
    @ColumnInfo(name = "churches_visited")
    val churchesVisited: Int = 0,
    @ColumnInfo(name = "sanctuaries_visited")
    val sanctuariesVisited: Int = 0,
    @ColumnInfo(name = "fortresses_visited")
    val fortressesVisited: Int = 0,
    @ColumnInfo(name = "tombs_visited")
    val tombsVisited: Int = 0,
    @ColumnInfo(name = "monuments_visited")
    val monumentsVisited: Int = 0,
    @ColumnInfo(name = "waterfalls_visited")
    val waterfallsVisited: Int = 0,
    @ColumnInfo(name = "others_visited")
    val othersVisited: Int = 0,
)
