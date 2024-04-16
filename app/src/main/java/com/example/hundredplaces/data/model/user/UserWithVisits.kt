package com.example.hundredplaces.data.model.user

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hundredplaces.data.model.visit.Visit
import java.time.LocalDateTime

data class UserWithVisits (
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id",
        entity = Visit::class
    )
    val visits: List<Visit>
)