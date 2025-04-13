package com.example.hundredplaces.ui.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hundredplaces.R
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.ui.AppViewModelProvider
import com.example.hundredplaces.ui.navigation.MenuNavigationDestination
import com.example.hundredplaces.ui.places.PlaceDistance
import com.example.hundredplaces.ui.places.PlaceRating
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.concurrent.Executors

object MapDestination : MenuNavigationDestination {
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
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel = viewModel  (
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState = mapViewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    if (context.checkSelfPermission(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
        fusedLocationClient.requestLocationUpdates(
            LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 10000).build(),
            Executors.newSingleThreadExecutor(),
            object: LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.lastLocation
                    if (location != null) {
                        mapViewModel.getDistances(location)
                    }
                }
            }
        )
    }

    GoogleMap(
        modifier = modifier
            .fillMaxSize(),
        cameraPositionState = rememberCameraPositionState{
            position = CameraPosition.fromLatLngZoom(LatLng(42.499912, 25.231491), 6.5f)
        },
        properties = MapProperties(
            isMyLocationEnabled = ContextCompat.checkSelfPermission(
                LocalContext.current,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    ) {
        for (place in uiState.places) {
            MarkerInfoWindow(
                state = remember { MarkerState(position = LatLng(place.place.latitude, place.place.longitude)) } ,
                infoWindowAnchor = Offset(0.5f, 0.65f),
                onInfoWindowClick = {
                    navigateToPlaceEntry(place.place.id)
                }
            ) {
                PlaceMapMarker(
                    placeWithCityAndImages = place,
                    distance = uiState.distances[place.place.id]
                )
            }
        }
    }
}

@Composable
fun PlaceMapMarker(
    placeWithCityAndImages: PlaceWithCityAndImages,
    distance: Float?,
    modifier: Modifier = Modifier
) {
    Card(
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
                    .data(
                        if (placeWithCityAndImages.images.isNotEmpty()) placeWithCityAndImages.images[0] else ""
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(MaterialTheme.shapes.medium)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = placeWithCityAndImages.place.name,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = placeWithCityAndImages.city,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    PlaceRating(
                        rating = placeWithCityAndImages.place.rating
                    )
                    PlaceDistance(
                        distance = distance
                    )
                }
            }
        }
    }
}