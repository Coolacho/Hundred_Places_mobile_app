package com.example.hundredplaces.ui.achievements

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.place.PlaceTypeEnum
import com.example.hundredplaces.data.model.visit.repositories.VisitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


class AchievementsViewModel(
    visitsRepository: VisitsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId = 1L //savedStateHandle["CURRENT_USER"]!!

    private val _hundredPlacesVisitsCount: Flow<Int> = visitsRepository.getVisitsCountByIsHundredPlacesAndUserId(userId)
    private val _placeTypeVisitsCount: Flow<Map<PlaceTypeEnum, Int>> = visitsRepository.getVisitsCountByPlaceTypeAndUserId(userId)

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
                    PlaceTypeEnum.CAVE -> AchievementTypeEnum.GALLERY
                    PlaceTypeEnum.CHURCH -> AchievementTypeEnum.CHURCH
                    PlaceTypeEnum.SANCTUARY -> AchievementTypeEnum.SANCTUARY
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