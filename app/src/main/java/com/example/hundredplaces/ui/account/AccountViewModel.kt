package com.example.hundredplaces.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.UserPreferencesRepository

import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.repositories.UsersDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userDataRepository: UsersDataRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState

    init {
        checkAutoLogin()
    }

    private fun checkAutoLogin() {
        viewModelScope.launch{
            if (userPreferencesRepository.prefUsername.first() != "") {
                val user = userDataRepository.getUserByEmail(userPreferencesRepository.prefUsername.first())
                _uiState.update {
                    it.copy(
                        isLoggedIn = true,
                        userDetails = user,
                        currentUser = user
                    )
                }
            }
        }
    }

    fun logIn(): Boolean {
        if (validateInput()) {
            viewModelScope.launch {
                val user = userDataRepository.getUserByEmailAndPassword(uiState.value.userDetails.email, uiState.value.userDetails.password)
                if (validateInput(user)) {
                    _uiState.update {
                        it.copy(
                            userDetails = user,
                            currentUser = user,
                            isLoggedIn = true
                        )
                    }
                    userPreferencesRepository.saveUsernamePreference(user.email)
                }
                else {
                    _uiState.update {
                        it.copy(
                            userDetails = User(name = "", email = "", password = ""),
                            isLoginSuccessful = false
                        )
                    }
                }
            }
            return uiState.value.isLoggedIn
        }
        return false
    }

    fun logOut() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    userDetails = User(name = "", email = "", password = ""),
                    currentUser = User(name = "", email = "", password = ""),
                    isLoggedIn = false
                )
            }
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

    fun createUser() {
        if (validateInput()) {
            viewModelScope.launch{
                userDataRepository.insertUser(uiState.value.userDetails)
                _uiState.update {
                    it.copy(
                        currentUser = uiState.value.userDetails,
                        isLoggedIn = true
                    )
                }
                userPreferencesRepository.saveUsernamePreference(uiState.value.userDetails.email)
            }
        }
    }

    fun updateUiState(userDetails: User) {
        _uiState.update {
            it.copy(
                userDetails = userDetails,
                isEntryValid = validateInput(userDetails),
            )
        }
    }

    fun validateInput(userDetails: User = uiState.value.userDetails): Boolean {
        return with(userDetails) {
            if (uiState.value.isLoggedIn) name.isNotBlank() && email.isNotBlank() && password.isNotBlank()
            else email.isNotBlank() && password.isNotBlank()
        }
    }
}