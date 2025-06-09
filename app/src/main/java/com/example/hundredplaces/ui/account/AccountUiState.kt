package com.example.hundredplaces.ui.account

data class AccountUiState(
    val userName: String = "",
    val userEmail: String = "",
    val isLoading: Boolean = false,
    val userMessage: Int? = null
)
