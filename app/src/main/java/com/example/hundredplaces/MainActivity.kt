package com.example.hundredplaces

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
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
import com.example.hundredplaces.ui.places.PlaceDetailsDestination
import com.example.hundredplaces.ui.places.PlacesDestination
import com.example.hundredplaces.ui.places.PlacesViewModel
import com.example.hundredplaces.ui.theme.HundredPlacesTheme
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HundredPlacesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val placesViewModel: PlacesViewModel = viewModel(
                        factory = AppViewModelProvider.Factory
                    )
                    val accountViewModel: AccountViewModel = viewModel (
                        factory = AppViewModelProvider.Factory
                    )

                    val windowSize = calculateWindowSizeClass(activity = this)
                    val navigateToPlaceDetails = intent.getBooleanExtra("navigateToPlacesDetails", false)
                    val placeId = intent.getLongExtra("placeId", 0L)
                    val startDestination = if (navigateToPlaceDetails && placeId != 0L) "${PlaceDetailsDestination.route}/${placeId}" else PlacesDestination.route

                    val locationPermissionLauncher = rememberLauncherForActivityResult(
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

                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationClient.requestLocationUpdates(
                            LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 10000).build(),
                            object: LocationCallback() {
                                override fun onLocationResult(locationResult: LocationResult) {
                                    for (location in locationResult.locations){
                                        placesViewModel.getDistances(location)
                                        placesViewModel.updateCameraPosition(location)
                                    }
                                }
                            },
                            Looper.getMainLooper()
                        )
                    }
                    else {
                        LaunchedEffect(Unit) {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                LaunchedEffect(Unit) {
                                    locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
                                }
                            }
                        }
                    }
                    HundredPlacesApp(
                        windowSize = windowSize.widthSizeClass,
                        startDestination = startDestination,
                        accountViewModel = accountViewModel,
                        placesViewModel = placesViewModel
                    )
                }
            }
        }
    }
}