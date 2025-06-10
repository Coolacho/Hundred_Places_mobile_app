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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hundredplaces.ui.HundredPlacesApp
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsDestination
import com.example.hundredplaces.ui.places.PlacesDestination
import com.example.hundredplaces.ui.theme.HundredPlacesTheme
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

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
                    val coroutineScope = rememberCoroutineScope(getContext = { Dispatchers.IO })

                    val userId = (application as HundredPlacesApplication).container.userRepository.userId.collectAsStateWithLifecycle().value
                    userId?.let {
                        LaunchedEffect(it) {
                            coroutineScope.launch {
                                (application as HundredPlacesApplication).container.visitRepository.pushVisits(userId)
                            }
                            coroutineScope.launch {
                                (application as HundredPlacesApplication).container.usersPlacesPreferencesRepository.pushUsersPlacesPreferences(userId)
                            }
                            //Update local data source after pushing local updates
                            coroutineScope.launch {
                                while (true) {
                                    (application as HundredPlacesApplication).container.visitRepository.pullVisits(userId)
                                    (application as HundredPlacesApplication).container.usersPlacesPreferencesRepository.pullUsersPlacesPreferences(userId)
                                    delay(60_000)
                                }
                            }
                        }
                    }

                    val windowSize = calculateWindowSizeClass(activity = this)
                    val navigateToPlaceDetails = intent.getBooleanExtra("navigateToPlacesDetails", false)
                    val placeId = intent.getLongExtra("placeId", 0L)
                    val startDestination: String = if (navigateToPlaceDetails && placeId != 0L) {
                        "${PlaceDetailsDestination.route}/${placeId}?addVisit=${true}"
                    } else PlacesDestination.route

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

                            permissions.getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false) -> {
                                // Background location access granted.
                                Log.d("LocationRequest", "Received Background location")
                            }

                            else -> {
                                // No location access granted.
                                Log.d("LocationRequest", "No location permission")
                            }
                        }
                    }

                    if (applicationContext.checkSelfPermission(
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
                        if (applicationContext.checkSelfPermission(
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            LaunchedEffect(Unit) {
                                permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
                            }
                        }
                    }
                    if (Build.VERSION.SDK_INT >= TIRAMISU) {
                        if (applicationContext.checkSelfPermission(
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            LaunchedEffect(Unit) {
                                permissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                            }
                        }
                    }
                    if (applicationContext.checkSelfPermission(
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        LaunchedEffect(Unit) {
                            permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
                        }
                    }

                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
                    if (applicationContext.checkSelfPermission(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationClient.requestLocationUpdates(
                            LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 10000).build(),
                            Executors.newSingleThreadExecutor(),
                            object: LocationCallback() {
                                override fun onLocationResult(locationResult: LocationResult) {
                                    val location = locationResult.lastLocation
                                    if (location != null) {
                                        (application as HundredPlacesApplication).container.distanceService.getDistances(location)
                                    }
                                }
                            }
                        )
                    }

                    if (applicationContext.checkSelfPermission(
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) {
                        (application as HundredPlacesApplication).container.workManagerRepository.addGeofences()
                    }

                    HundredPlacesApp(
                        windowSize = windowSize.widthSizeClass,
                        startDestination = startDestination,
                        userId = userId
                    )
                }
            }
        }
    }
}