package com.example.hundredplaces.ui.account

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.R
import com.example.hundredplaces.data.UserAppPreferencesRepository
import com.example.hundredplaces.data.model.user.repositories.UserRepository
import com.example.hundredplaces.util.hashPassword
import com.example.hundredplaces.util.validateEmail
import com.example.hundredplaces.util.validateName
import com.example.hundredplaces.util.validatePassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userRepository: UserRepository,
    private val userAppPreferencesRepository: UserAppPreferencesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState

    val userId = userRepository.userId

    val nameState = TextFieldState()
    val emailState = TextFieldState()
    val oldPasswordState = TextFieldState()
    val newPasswordState = TextFieldState()
    val repeatNewPasswordState = TextFieldState()

    val nameHasErrors by derivedStateOf { validateName(nameState.text) }
    val emailHasErrors by derivedStateOf { validateEmail(emailState.text) }
    val oldPasswordHasErrors by derivedStateOf { validatePassword(oldPasswordState.text) }
    val newPasswordHasErrors by derivedStateOf { validatePassword(newPasswordState.text) && (newPasswordState.text != oldPasswordState.text) }
    val repeatNewPasswordHasErrors by derivedStateOf { validatePassword(repeatNewPasswordState.text) && (repeatNewPasswordState.text != newPasswordState.text) }

    fun logOut() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    userName = "",
                    userEmail = ""
                )
            }
            userAppPreferencesRepository.saveUsernamePreference("")
            userRepository.logOut()
        }
    }

    fun pullUser() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = userRepository.pullUser()
            _uiState.update { it.copy(isLoading = false) }
            result?.let {
                _uiState.update {
                    it.copy(
                        userName = result.first,
                        userEmail = result.second
                    )
                }
            }
        }
    }

    fun updateUser() {
        if (canUpdateAccount()) {
            viewModelScope.launch {
                if (userRepository.updateUserDetails(nameState.text.toString(), emailState.text.toString())) {
                    userAppPreferencesRepository.saveUsernamePreference(emailState.text.toString())
                    showSnackbarMessage(R.string.user_updated_successfully)
                }
                else showSnackbarMessage(R.string.user_update_failed)
                nameState.clearText()
                emailState.clearText()
            }
        }
    }

    fun updatePassword() {
        if (canUpdatePassword()) {
            val hashedOldPassword = hashPassword(oldPasswordState.text.toString().toByteArray())
            val hashedNewPassword = hashPassword(newPasswordState.text.toString().toByteArray())
            val hexHashedOldPassword = hashedOldPassword.joinToString("") { "%02x".format(it) }
            val hexHashedNewPassword = hashedNewPassword.joinToString("") { "%02x".format(it) }
            viewModelScope.launch {
                if (userRepository.updateUserPassword(hexHashedOldPassword, hexHashedNewPassword)) {
                    showSnackbarMessage(R.string.user_updated_successfully)
                }
                else showSnackbarMessage(R.string.user_update_failed)
                oldPasswordState.clearText()
                newPasswordState.clearText()
                repeatNewPasswordState.clearText()
            }
        }
    }

    fun canUpdateAccount(): Boolean {
        return !(nameHasErrors || emailHasErrors ||
                nameState.text.isEmpty() || emailState.text.isEmpty())
    }

    fun canUpdatePassword(): Boolean {
        return !(oldPasswordHasErrors || newPasswordHasErrors || repeatNewPasswordHasErrors ||
                oldPasswordState.text.isEmpty() || newPasswordState.text.isEmpty() || repeatNewPasswordState.text.isEmpty())
    }

    fun snackbarMessageShown() {
        _uiState.update {
            it.copy(
                userMessage = null
            )
        }
    }

    private fun showSnackbarMessage(message: Int) {
        _uiState.update {
            it.copy(
                userMessage = message
            )
        }
    }
}