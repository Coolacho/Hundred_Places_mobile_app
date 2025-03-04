package com.example.hundredplaces.ui.account

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.UserPreferencesRepository
import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.repositories.UsersRepository
import com.example.hundredplaces.workers.WorkManagerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userRepository: UsersRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val workManagerRepository: WorkManagerRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState

    init {
        checkAutoLogin()
    }

    private fun checkAutoLogin() {
        viewModelScope.launch{
            if (userPreferencesRepository.prefUsername.first() != "") {
                val user = userRepository.getUserByEmail(userPreferencesRepository.prefUsername.first())
                _uiState.update {
                    it.copy(
                        isLoggedIn = true,
                        userDetails = user,
                        currentUser = user
                    )
                }
                setUserId(user.id)
                workManagerRepository.startSync(user.id)
            }
        }
    }

    fun logIn(): Boolean {
        if (validateInput()) {
            viewModelScope.launch {
                val user = userRepository.getUserByEmailAndPassword(uiState.value.userDetails.email, uiState.value.userDetails.password)
                if (validateInput(user)) {
                    _uiState.update {
                        it.copy(
                            userDetails = user,
                            currentUser = user,
                            isLoggedIn = true
                        )
                    }
                    userPreferencesRepository.saveUsernamePreference(user.email)
                    workManagerRepository.startSync(user.id)
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
                userRepository.updateUser(uiState.value.userDetails)
            }
        }
    }

    fun createUser() {
        if (validateInput()) {
            viewModelScope.launch{
                userRepository.insertUser(uiState.value.userDetails)
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

    fun setUserId(id: Long) {
        savedStateHandle["CURRENT_USER"] = id
    }
}