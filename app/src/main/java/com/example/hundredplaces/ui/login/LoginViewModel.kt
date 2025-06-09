package com.example.hundredplaces.ui.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.R
import com.example.hundredplaces.data.UserAppPreferencesRepository
import com.example.hundredplaces.data.model.image.repositories.ImageRepository
import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.repositories.UserRepository
import com.example.hundredplaces.util.hashPassword
import com.example.hundredplaces.util.validateEmail
import com.example.hundredplaces.util.validateName
import com.example.hundredplaces.util.validatePassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val userAppPreferencesRepository: UserAppPreferencesRepository,
    imageRepository: ImageRepository,
): ViewModel() {

    init {
        autoLogin()
    }

    private val _isFlipped = MutableStateFlow(false)
    private val _userMessage = MutableStateFlow<Int?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _userId = userRepository.userId
    private val _images = imageRepository.oneImagePerPlace

    val uiState: StateFlow<LoginUiState> = combine(
        _isFlipped,_userMessage, _isLoading, _userId, _images
    ) { isFlipped, userMessage, isLoading, userId, images ->
        LoginUiState(
            isLoading = isLoading,
            userMessage = userMessage,
            isFlipped = isFlipped,
            userId = userId,
            images = images
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LoginUiState()
        )

    val nameState = TextFieldState()
    val emailState = TextFieldState()
    val passwordState = TextFieldState()

    val nameHasErrors by derivedStateOf { validateName(nameState.text) }
    val emailHasErrors by derivedStateOf { validateEmail(emailState.text) }
    val passwordHasErrors by derivedStateOf { validatePassword(passwordState.text) }

    private fun autoLogin() {
        viewModelScope.launch{
            val username = userAppPreferencesRepository.prefUsername.first()
            if (username.isNotEmpty()) {
                _isLoading.value = true
                userRepository.getUserByEmail(userAppPreferencesRepository.prefUsername.first())
                _isLoading.value = false
            }
        }
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    fun changeForm() {
        _isFlipped.value = !_isFlipped.value
        nameState.clearText()
        emailState.clearText()
        passwordState.clearText()
    }

    fun logIn() {
        if (canLogIn()) {
            viewModelScope.launch {
                _isLoading.value = true
                val hashedPassword = hashPassword(passwordState.text.toString().toByteArray())
                val hexHashedPassword = hashedPassword.joinToString("") { "%02x".format(it) }
                userRepository.getUserByEmailAndPassword(emailState.text.toString(), hexHashedPassword)
                _isLoading.value = false
                val userId = _userId.value
                if (userId != null) {
                    userAppPreferencesRepository.saveUsernamePreference(emailState.text.toString())
                } else showSnackbarMessage(R.string.wrong_email_password)
                emailState.clearText()
                passwordState.clearText()
            }
        }
    }

    fun createUser() {
        if (canCreateAccount()) {
            val hashedPassword = hashPassword(passwordState.text.toString().toByteArray())
            val hexHashedPassword = hashedPassword.joinToString("") { "%02x".format(it) }
            val user = User(
                name = nameState.text.toString(),
                email = emailState.text.toString(),
                password = hexHashedPassword
            )

            viewModelScope.launch {
                _isLoading.value = true
                if (userRepository.insertUser(user)) {
                    userAppPreferencesRepository.saveUsernamePreference(user.email)
                } else showSnackbarMessage(R.string.user_creation_failed)
                _isLoading.value = false
                nameState.clearText()
                emailState.clearText()
                passwordState.clearText()
            }
        }
    }

    fun canLogIn(): Boolean {
        return !(emailHasErrors || passwordHasErrors
                || emailState.text.isEmpty() || passwordState.text.isEmpty())
    }

    fun canCreateAccount(): Boolean {
        return !(nameHasErrors || emailHasErrors || passwordHasErrors ||
                nameState.text.isEmpty() || emailState.text.isEmpty() || passwordState.text.isEmpty())
    }
}