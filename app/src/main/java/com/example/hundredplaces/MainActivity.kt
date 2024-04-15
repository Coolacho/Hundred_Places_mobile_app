package com.example.hundredplaces

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hundredplaces.ui.HundredPlacesApp
import com.example.hundredplaces.ui.places.PlacesDestination
import com.example.hundredplaces.ui.places.details.PlaceDetailsDestination
import com.example.hundredplaces.ui.theme.HundredPlacesTheme

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
                    val windowSize = calculateWindowSizeClass(activity = this)
                    val navigateToPlaceDetails = intent.getBooleanExtra("navigateToPlacesDetails", false)
                    val startDestination = if (navigateToPlaceDetails) PlaceDetailsDestination.routeWithArgs else PlacesDestination.route
                    HundredPlacesApp(
                        windowSize = windowSize.widthSizeClass,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HundredPlacesAppCompatPreview() {
    HundredPlacesTheme {
        Surface {
            HundredPlacesApp(
                windowSize = WindowWidthSizeClass.Compact,
                startDestination = PlacesDestination.route
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun HundredPlacesAppMediumPreview() {
    HundredPlacesTheme {
        Surface {
            HundredPlacesApp(
                windowSize = WindowWidthSizeClass.Compact,
                startDestination = PlacesDestination.route
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun HundredPlacesAppExpandedPreview() {
    HundredPlacesTheme {
        Surface {
            HundredPlacesApp(
                windowSize = WindowWidthSizeClass.Compact,
                startDestination = PlacesDestination.route
            )
        }
    }
}