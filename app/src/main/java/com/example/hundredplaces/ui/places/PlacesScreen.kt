package com.example.hundredplaces.ui.places

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.navigation.NavigationDestination

object PlacesDestination : NavigationDestination {
    override val route = "Places"
    override val iconRes = R.drawable.rounded_museum_24
}

/**
 * Entry route for Places screen
 */
@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier
) {
    Row {

    }
}