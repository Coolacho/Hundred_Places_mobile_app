package com.example.hundredplaces.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.UserPreferencesRepository

import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.repositories.UsersDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userDataRepository: UsersDataRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState

    fun logOut() {
        viewModelScope.launch {
            userPreferencesRepository.saveUsernamePreference("")
        }
    }

    fun saveUser() {
        if (validateInput()) {
            viewModelScope.launch {
                userDataRepository.updateUser(uiState.value.userDetails)
            }
        }
    }

    fun updateUiState(userDetails: User) {
        _uiState.update {
            it.copy(
                userDetails = userDetails,
                isEntryValid = validateInput(userDetails)
            )
        }
    }

    private fun validateInput(userDetails: User = uiState.value.userDetails): Boolean {
        return with(userDetails) {
            name.isNotBlank() && email.isNotBlank() && password.isNotBlank()
        }
    }
}