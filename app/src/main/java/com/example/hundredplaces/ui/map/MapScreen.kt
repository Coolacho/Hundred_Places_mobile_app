package com.example.hundredplaces.ui.map

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hundredplaces.R
import com.example.hundredplaces.data.FakePlaceDataSource
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.ui.navigation.NavigationDestination
import com.example.hundredplaces.ui.theme.HundredPlacesTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

object MapDestination : NavigationDestination {
    override val route = "Map"
    override val iconRes = R.drawable.rounded_map_24
}

/**
 * Entry route for Map screen
 */
@Composable
fun MapScreen(
    navigateToPlaceEntry: (Int) -> Unit,
    modifier: Modifier = Modifier,
    placesWithCityAndImages: List<PlaceWithCityAndImages> = FakePlaceDataSource.PlacesList
) {
    val sofia = LatLng(42.65542986025372, 23.271044935725428)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(sofia, 10f)
    }
    GoogleMap(
        modifier = modifier
            .fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        for (place in placesWithCityAndImages) {
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
            Image(
                painter = painterResource(id = R.drawable.nim),
                contentDescription = null,
                contentScale = ContentScale.Crop,
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