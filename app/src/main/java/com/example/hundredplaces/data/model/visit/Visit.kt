package com.example.hundredplaces.data.model.visit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.user.User
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
@Entity(
    tableName = "visits",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Place::class,
            parentColumns = ["id"],
            childColumns = ["place_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Visit(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private val id: Int = 0,
    @ColumnInfo(name = "user_id")
    private val userId: Int,
    @ColumnInfo(name = "place_id")
    private val placeId: Int,
    @Contextual
    @ColumnInfo(name = "date_visited")
    private val dateVisited: LocalDateTime
)
