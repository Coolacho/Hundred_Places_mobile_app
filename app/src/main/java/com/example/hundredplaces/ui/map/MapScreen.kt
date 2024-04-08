package com.example.hundredplaces.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.navigation.NavigationDestination
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

object MapDestination : NavigationDestination {
    override val route = "Map"
    override val iconRes = R.drawable.rounded_map_24
}

/**
 * Entry route for Map screen
 */
@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            MapView(context).apply {
                id = R.id.map_view
            }
        }
    ) { mapView ->
        mapView.getMapAsync { googleMap ->
            val coordinates = LatLng(37.7749, -122.4194) // San Francisco
            googleMap.addMarker(
                MarkerOptions().position(coordinates).title("Marker in San Francisco")
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates))
        }
    }
}