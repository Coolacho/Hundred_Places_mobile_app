package com.example.hundredplaces.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface MenuNavigationDestination : NavigationDestination {
    /**
     * String resource for navigation menu labels
     */
    @get:StringRes
    val title: Int

    /**
     * Icon for menu
     */
    @get:DrawableRes
    val iconRes: Int
}