package com.example.hundredplaces

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hundredplaces.ui.AppViewModelProvider
import com.example.hundredplaces.ui.HundredPlacesApp
import com.example.hundredplaces.ui.account.AccountViewModel
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsDestination
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsViewModel
import com.example.hundredplaces.ui.places.PlacesDestination
import com.example.hundredplaces.ui.theme.HundredPlacesTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HundredPlacesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val accountViewModel: AccountViewModel = viewModel (
                        factory = AppViewModelProvider.Factory
                    )
                    val placeDetailsViewModel: PlaceDetailsViewModel = viewModel (
                        factory = AppViewModelProvider.Factory
                    )

                    val windowSize = calculateWindowSizeClass(activity = this)
                    val navigateToPlaceDetails = intent.getBooleanExtra("navigateToPlacesDetails", false)
                    val placeId = intent.getLongExtra("placeId", 0L)
                    val startDestination: String
                    if (navigateToPlaceDetails && placeId != 0L) {
                        startDestination = "${PlaceDetailsDestination.route}/${placeId}"
                        placeDetailsViewModel.setPlaceId(placeId)
                    } else startDestination = PlacesDestination.route

                    val permissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestMultiplePermissions()
                    )
                    { permissions ->
                        when {
                            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                                // Precise location access granted.
                                Log.d("LocationRequest", "Received Fine location")
                            }

                            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                                // Only approximate location access granted.
                                Log.d("LocationRequest", "Received Coarse location")
                            }

                            else -> {
                                // No location access granted.
                                Log.d("LocationRequest", "No location permission")
                            }
                        }
                    }

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED) {
                        LaunchedEffect(Unit) {
                            permissionLauncher.launch(
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                    )
                                }
                                else {
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                }
                            )
                        }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            LaunchedEffect(Unit) {
                                permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
                            }
                        }
                    }
                    if (Build.VERSION.SDK_INT >= TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            LaunchedEffect(Unit) {
                                permissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                            }
                        }
                    }
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        LaunchedEffect(Unit) {
                            permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
                        }
                    }

                    HundredPlacesApp(
                        windowSize = windowSize.widthSizeClass,
                        startDestination = startDestination,
                        accountViewModel = accountViewModel,
                        placeDetailsViewModel = placeDetailsViewModel,
                    )
                }
            }
        }
    }
}