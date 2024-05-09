package com.example.hundredplaces.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Interface to describe the navigation destinations for the app
 */
interface NavigationDestination {
    /**
     * Unique String to define the path for a composable and to be displayed for the screen.
     */
    val route: String

}