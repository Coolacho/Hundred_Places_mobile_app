package com.example.hundredplaces.ui.map

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hundredplaces.R
import com.example.hundredplaces.data.FakePlaceDataSource
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.ui.navigation.NavigationDestination
import com.example.hundredplaces.ui.places.PlacesUiState
import com.example.hundredplaces.ui.theme.HundredPlacesTheme
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

object MapDestination : NavigationDestination {
    override val route = "Map"
    override val title = R.string.map
    override val iconRes = R.drawable.rounded_map_24
}

/**
 * Entry route for Map screen
 */
@Composable
fun MapScreen(
    navigateToPlaceEntry: (Long) -> Unit,
    uiState: PlacesUiState,
    modifier: Modifier = Modifier
) {

    var currentLocation by remember { mutableStateOf("Your location") }
    val context = LocalContext.current
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions())
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
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val long = location.longitude
                    currentLocation = "Latitude: $lat, Longitude: $long"
                }
            }
            .addOnFailureListener { exception ->
                // Handle location retrieval failure
                exception.printStackTrace()
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                LaunchedEffect(Unit) {
                    locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
                }
            }
        }
    }
    else {
        LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))}
    }

    val geofencingClient = LocationServices.getGeofencingClient(context)
    val geofenceList = mutableListOf<Geofence>()
    for (place in uiState.places) {
        geofenceList.add(Geofence.Builder()
            .setRequestId(place.place.id.toString())
            .setCircularRegion(
                place.place.latitude,
                place.place.longitude,
                100f
            )
            .setExpirationDuration(3600000)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
            .setLoiteringDelay(60000) //Add one 0 for 10 minutes
            .setNotificationResponsiveness(300000)
            .build()
        )
    }
    val geofencingRequest = GeofencingRequest.Builder()
        .addGeofences(geofenceList)
        .build()

    // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
    // addGeofences() and removeGeofences().
    val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
    }

    geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
        addOnSuccessListener {
            // Geofences added
            Log.d("Geofences", "Geofences added")
        }
        addOnFailureListener {
            // Failed to add geofences
                exception ->
            // Failed to add geofences
            val errorMessage = when (exception) {
                is ApiException -> {
                    // Get the status code for the exception
                    // Convert status code to human-readable message
                    when (val statusCode = exception.statusCode) {
                        GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> "Geofence service is not available now"
                        GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> "You have exceeded the maximum number of geofences"
                        GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> "You have exceeded the maximum number of pending intents"
                        else -> "Error code: $statusCode"
                    }
                }
                else -> exception.message ?: "Unknown error"
            }
            Log.e("Geofences", "Failed to add geofences: $errorMessage")
        }
    }

    val sofia = LatLng(42.65542986025372, 23.271044935725428)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(sofia, 10f)
    }
    GoogleMap(
        modifier = modifier
            .fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    ) {
        for (place in uiState.places) {
            MarkerInfoWindow(
                state = MarkerState(position = LatLng(place.place.latitude, place.place.longitude)),
                onInfoWindowClick = { navigateToPlaceEntry(place.place.id) }
            ){
                PlaceMapMarker(place)
            }
        }
    }
}

@Composable
fun PlaceMapMarker(
    placeWithCityAndImages: PlaceWithCityAndImages,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .width(300.dp)
            .height(200.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .allowHardware(false)
                    .data(placeWithCityAndImages.images[0])
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = placeWithCityAndImages.place.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = placeWithCityAndImages.city,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/**
 * Preview for place marker card
 */
@Preview
@Composable
private fun PlaceMapMarkerPreview() {
    HundredPlacesTheme {
        Surface {
            PlaceMapMarker(FakePlaceDataSource.PlacesList[0])
        }
    }
}