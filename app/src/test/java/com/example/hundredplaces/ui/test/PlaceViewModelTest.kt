package com.example.hundredplaces.ui.test

import com.example.hundredplaces.data.FakePlaceDataSource
import com.example.hundredplaces.data.repositories.TestCityRepository
import com.example.hundredplaces.data.repositories.TestImageRepository
import com.example.hundredplaces.data.repositories.TestPlaceRepository
import com.example.hundredplaces.data.repositories.TestUserRepository
import com.example.hundredplaces.data.repositories.TestUsersPlacesPreferencesRepository
import com.example.hundredplaces.ui.places.PlaceFiltersEnum
import com.example.hundredplaces.ui.places.PlacesViewModel
import com.example.hundredplaces.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlaceViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var placesViewModel: PlacesViewModel

    private val placeRepository = TestPlaceRepository()

    @Before
    fun setupViewModel() {
        placesViewModel = PlacesViewModel(
            placeRepository = placeRepository,
            cityRepository = TestCityRepository(),
            imageRepository = TestImageRepository(),
            userRepository = TestUserRepository(),
            usersPlacesPreferencesRepository = TestUsersPlacesPreferencesRepository()
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiStatePlaces_WhenFilteringByName_thenShowCorrectPlace() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { placesViewModel.uiState.collect {} }

        placeRepository.sendPlacesWithCityAndImages(FakePlaceDataSource.placesWithCityAndImagesList)

        val place = FakePlaceDataSource.placesWithCityAndImagesList[0]
        placesViewModel.onSearchTextChange(place.place.name)

        val uiState = placesViewModel.uiState.value
        assertEquals(uiState.filteredPlaces.size, 1)
        assertEquals(uiState.filteredPlaces[0], place)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiStatePlaces_WhenFilteringByType_thenShowCorrectPlace() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { placesViewModel.uiState.collect {} }

        placeRepository.sendPlacesWithCityAndImages(FakePlaceDataSource.placesWithCityAndImagesList)

        val place = FakePlaceDataSource.placesWithCityAndImagesList[3]
        placesViewModel.toggleFilter(PlaceFiltersEnum.FORTRESS)

        val uiState = placesViewModel.uiState.value
        assertEquals(uiState.filteredPlaces.size, 1)
        assertEquals(uiState.filteredPlaces[0], place)
    }
}