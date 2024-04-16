package com.example.hundredplaces.data.model.user

import androidx.room.Embedded
import androidx.room.Relation
import java.time.LocalDateTime

data class UserWithVisits (
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "id",
        projection = ["place_id, date_visited"],
        entity = User::class
    )
    val visits: Map<Long, List<LocalDateTime>>
)