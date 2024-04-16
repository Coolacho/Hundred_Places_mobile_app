package com.example.hundredplaces.data.model.visit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.user.User
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
@Entity(
    tableName = "visits",
    indices = [
        Index(
            value = ["user_id"]
        ),
        Index(
            value = ["place_id"]
        )
    ],
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
    val id: Long = 0,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "place_id")
    val placeId: Long,
    @Contextual
    @ColumnInfo(name = "date_visited")
    val dateVisited: LocalDateTime
)
