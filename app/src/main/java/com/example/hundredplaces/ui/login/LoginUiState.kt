package com.example.hundredplaces.ui.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val isFlipped: Boolean = false,
    val userMessage: Int? = null,
    val userId: Long? = null,
    val images: List<String> = emptyList(),
)
