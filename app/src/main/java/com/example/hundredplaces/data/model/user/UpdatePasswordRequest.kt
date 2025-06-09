package com.example.hundredplaces.data.model.user

data class UpdatePasswordRequest(
    val user: User,
    val oldPassword: String
)
