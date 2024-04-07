package com.example.hundredplaces.ui.navigation

import androidx.annotation.DrawableRes

/**
 * Interface to describe the navigation destinations for the app
 */
interface NavigationDestination {
    /**
     * Unique String resource id to define the path for a composable and to be displayed for the screen.
     */
    val route: String

    /**
     * Icon for menu
     */
    @get:DrawableRes
    val iconRes: Int
}