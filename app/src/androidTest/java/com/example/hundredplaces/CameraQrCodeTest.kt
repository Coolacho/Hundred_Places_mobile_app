package com.example.hundredplaces

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.example.hundredplaces.data.repositories.TestLandmarkService
import com.example.hundredplaces.data.repositories.TestPlaceRepository
import com.example.hundredplaces.data.repositories.TestUserRepository
import com.example.hundredplaces.data.repositories.TestVisitRepository
import com.example.hundredplaces.ui.camera.CameraScreenDestination
import com.example.hundredplaces.ui.camera.CameraViewModel
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsDestination
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsScreen
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CameraQrCodeTest {

    @get:Rule val composeTestRule = createComposeRule()

    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var placeDetailsViewModel: PlaceDetailsViewModel
    private lateinit var navController: TestNavHostController

    private val placeRepository = TestPlaceRepository()

    @Before
    fun setup() {
        composeTestRule.setContent {
            cameraViewModel = CameraViewModel(
                landmarkService = TestLandmarkService(),
                placeRepository = placeRepository
            )

            navController = TestNavHostController(ApplicationProvider.getApplicationContext()).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            NavHost(
                navController = navController,
                startDestination = CameraScreenDestination.route
            ) {
                composable(
                    route = CameraScreenDestination.route
                ) {
                    val uiState = cameraViewModel.uiState.collectAsStateWithLifecycle().value
                    uiState.qrContent?.lastPathSegment?.let {
                        LaunchedEffect(it) {
                            navController.navigate("${PlaceDetailsDestination.route}/${it.toLong()}?addVisit=${true}")
                            cameraViewModel.qrContentRead()
                            Log.d("CAMERA", "NAVIGATION IS HAPPENING")
                        }
                    }
                }
                composable(
                    route = PlaceDetailsDestination.routeWithArgs,
                    arguments = listOf(
                        navArgument(PlaceDetailsDestination.PLACE_ID_ARG) { type = NavType.LongType },
                        navArgument(PlaceDetailsDestination.ADD_VISIT_ARG) { type = NavType.BoolType })
                ) { navBackStackEntry ->

                    val placeId = navBackStackEntry.arguments?.getLong(PlaceDetailsDestination.PLACE_ID_ARG)
                    val addVisit = navBackStackEntry.arguments?.getBoolean(PlaceDetailsDestination.ADD_VISIT_ARG) == true

                    placeId?.let {
                        placeDetailsViewModel = PlaceDetailsViewModel(
                            placeRepository = placeRepository,
                            userRepository = TestUserRepository(),
                            visitRepository = TestVisitRepository(),
                            _placeId = it
                        )

                        PlaceDetailsScreen(
                            isFullScreen = false,
                            addVisit = addVisit,
                            placesDetailsViewModel = placeDetailsViewModel,
                            navigateBack = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }

    @Test
    fun qrCodeScan_whenQRCodeScanned_thenNavigateToPlaceDetails() = runTest {

        val context = InstrumentationRegistry.getInstrumentation().context
        val inputStream = context.assets.open("test_qr_code_valid.png")
        val bitmap = BitmapFactory.decodeStream(inputStream)
        cameraViewModel.analyzeImage(bitmap)

        composeTestRule.awaitIdle()

        val route = navController.currentDestination?.route

        assertEquals(PlaceDetailsDestination.routeWithArgs, route)
    }

    @Test
    fun qrCodeScan_whenQRCodeNotScanned_thenStayOnCameraScreen() = runTest {

        val context = InstrumentationRegistry.getInstrumentation().context
        val inputStream = context.assets.open("test_qr_code_not_valid.png")
        val bitmap = BitmapFactory.decodeStream(inputStream)
        cameraViewModel.analyzeImage(bitmap)

        composeTestRule.awaitIdle()

        val route = navController.currentDestination?.route

        assertEquals(CameraScreenDestination.route, route)
    }

}