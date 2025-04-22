package com.example.hundredplaces.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.UserAppPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppContentViewModel(
    private val userAppPreferencesRepository: UserAppPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppContentUiState())

    // UI states access for various [HomeUiState]
    val uiState: StateFlow<AppContentUiState> =
        userAppPreferencesRepository.isLinearLayout
            .map { isLinearLayout ->
                AppContentUiState(isLinearLayout = isLinearLayout)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AppContentUiState()
            )

    /*
     * [selectLayout] change the layout and icons accordingly and
     * save the selection in DataStore through [userPreferencesRepository]
     */
    fun selectLayout() {
        viewModelScope.launch {
            userAppPreferencesRepository.saveLayoutPreference(!_uiState.value.isLinearLayout)
        }
    }

}