package com.example.hundredplaces.ui.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.place.PlaceTypeEnum
import com.example.hundredplaces.data.model.user.repositories.UserRepository
import com.example.hundredplaces.data.model.visit.repositories.VisitRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class AchievementsViewModel(
    visitRepository: VisitRepository,
    userRepository: UserRepository
) : ViewModel() {

    private val _userId = userRepository.userId

    private val _hundredPlacesVisitsCount: Flow<Int> = _userId.flatMapLatest { userId ->
        userId?.let { visitRepository.getVisitsCountByIsHundredPlacesAndUserId(userId) } ?: flowOf(0)
    }

    private val _placeTypeVisitsCount: Flow<Map<PlaceTypeEnum, Int>> = _userId.flatMapLatest { userId ->
        userId?.let { visitRepository.getVisitsCountByPlaceTypeAndUserId(userId) } ?: flowOf(emptyMap())
    }

    val uiState = combine(
       _hundredPlacesVisitsCount,
        _placeTypeVisitsCount
    ) { hundredPlacesVisits, placeTypeVisits ->
        //Adding a default map with all PlaceTypes and 0 visits,
        //because GROUP BY does not include 0 values
        val allPlaceTypes = PlaceTypeEnum.entries.associateWith { 0 }

        //Combining the flows' values into a single complete achievements map
        val achievements = buildMap {
            put(AchievementTypeEnum.HUNDRED_PLACES, hundredPlacesVisits)

            val completePlaceTypeVisits = allPlaceTypes + placeTypeVisits
            putAll(completePlaceTypeVisits.mapKeys {
                when (it.key) {
                    PlaceTypeEnum.MUSEUM -> AchievementTypeEnum.MUSEUM
                    PlaceTypeEnum.PEAK -> AchievementTypeEnum.PEAK
                    PlaceTypeEnum.GALLERY -> AchievementTypeEnum.GALLERY
                    PlaceTypeEnum.CAVE -> AchievementTypeEnum.CAVE
                    PlaceTypeEnum.CHURCH -> AchievementTypeEnum.CHURCH
                    PlaceTypeEnum.RESERVE -> AchievementTypeEnum.RESERVE
                    PlaceTypeEnum.FORTRESS -> AchievementTypeEnum.FORTRESS
                    PlaceTypeEnum.TOMB -> AchievementTypeEnum.TOMB
                    PlaceTypeEnum.MONUMENT -> AchievementTypeEnum.MONUMENT
                    PlaceTypeEnum.WATERFALL -> AchievementTypeEnum.WATERFALL
                    PlaceTypeEnum.OTHER -> AchievementTypeEnum.OTHER
                }
            })

            put(AchievementTypeEnum.TOTAL, placeTypeVisits.values.sum())
        }

        AchievementsUiState(
            achievements
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AchievementsUiState()
        )
}