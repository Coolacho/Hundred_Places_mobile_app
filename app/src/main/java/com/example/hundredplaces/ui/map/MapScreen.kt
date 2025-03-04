package com.example.hundredplaces.ui.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hundredplaces.R
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.ui.navigation.MenuNavigationDestination
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState

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
    mapViewModel: MapViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = mapViewModel.uiState.collectAsStateWithLifecycle().value

    GoogleMap(
        modifier = modifier
            .fillMaxSize(),
        cameraPositionState = uiState.cameraPositionState ,
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