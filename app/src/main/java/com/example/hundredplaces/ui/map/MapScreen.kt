package com.example.hundredplaces.ui.map

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.navigation.NavigationDestination

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
    Row {
        Text(text = "This is the map screen")
    }
}