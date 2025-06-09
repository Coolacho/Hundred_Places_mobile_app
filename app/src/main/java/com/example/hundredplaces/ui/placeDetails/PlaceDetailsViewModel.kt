package com.example.hundredplaces.ui.placeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.data.model.place.repositories.PlaceRepository
import com.example.hundredplaces.data.model.user.repositories.UserRepository
import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.repositories.VisitRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class PlaceDetailsViewModel(
    placeRepository: PlaceRepository,
    userRepository: UserRepository,
    private val visitRepository: VisitRepository,
    private val _placeId: Long
) : ViewModel() {

    private val _userId = userRepository.userId

    private val _visits = _userId.flatMapLatest { userId ->
        userId?.let { visitRepository.getAllVisitDatesByUserIdAndPlaceId(userId, _placeId) } ?: flowOf(emptyList())
    }
    private val _place: Flow<PlaceWithCityAndImages?> = placeRepository.getPlaceWithCityAndImages(_placeId)
    private val _descriptionText = MutableStateFlow<String?>(null)


    val uiState = combine(
        _descriptionText, _visits, _place
    ) { descriptionText, visits, place ->
        if (place == null) {
            PlaceDetailsUiState.Error
        }
        else {
            PlaceDetailsUiState.Success(
                place = place,
                descriptionText = descriptionText,
                visits = visits
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PlaceDetailsUiState.Loading
        )

    suspend fun addVisit() : Boolean {
        return visitRepository.insertVisit(
            Visit(
                userId = _userId.value!!,
                placeId = _placeId
            )
        )
    }

    fun updateDescriptionText(text: String?) {
        _descriptionText.value = text
    }

}