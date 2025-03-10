package com.example.hundredplaces.data.model.visit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.user.User
import java.time.Instant
import java.util.UUID

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
data class Visit (
    @PrimaryKey()
    @ColumnInfo(name = "id")
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "place_id")
    val placeId: Long,
    @ColumnInfo(name = "date_visited")
    val dateVisited: Instant = Instant.now()
)
