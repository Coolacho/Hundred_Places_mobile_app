package com.example.hundredplaces.ui.test

import com.example.hundredplaces.data.repositories.TestPlaceRepository
import com.example.hundredplaces.data.repositories.TestUserRepository
import com.example.hundredplaces.data.repositories.TestVisitRepository
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsViewModel
import com.example.hundredplaces.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlaceDetailsViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var placeDetailsViewModel: PlaceDetailsViewModel

    private val userRepository = TestUserRepository()
    private val visitRepository = TestVisitRepository()

    @Before
    fun setupViewModel() {
        placeDetailsViewModel = PlaceDetailsViewModel(
            placeRepository = TestPlaceRepository(),
            userRepository = userRepository,
            visitRepository = visitRepository,
            _placeId = 1
        )
    }

    @Test
    fun viewModel_WhenAddingVisit_thenVisitAdded() = runTest {
        userRepository.setUserId(1)

        visitRepository.deleteAllVisits()
        placeDetailsViewModel.addVisit()

        val visits = visitRepository.visits.value

        assertEquals(1, visits.size)
    }
}