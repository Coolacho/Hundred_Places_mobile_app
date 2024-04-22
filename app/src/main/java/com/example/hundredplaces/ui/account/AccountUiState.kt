package com.example.hundredplaces.ui.account

import com.example.hundredplaces.data.model.user.User

data class AccountUiState(
    val isEntryValid: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isLoginSuccessful: Boolean = true,
    val userDetails: User = User(name = "", email = "", password = ""),
    val currentUser: User = User(name = "", email = "", password = "")
)
