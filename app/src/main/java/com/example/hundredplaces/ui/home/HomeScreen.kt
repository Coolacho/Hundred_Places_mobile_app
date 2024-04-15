package com.example.hundredplaces.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.AppContentType
import com.example.hundredplaces.ui.AppNavigationType
import com.example.hundredplaces.ui.account.AccountDestination
import com.example.hundredplaces.ui.achievements.AchievementsDestination
import com.example.hundredplaces.ui.map.MapDestination
import com.example.hundredplaces.ui.navigation.AppBottomNavigationBar
import com.example.hundredplaces.ui.navigation.AppNavigationRail
import com.example.hundredplaces.ui.navigation.HundredPlacesNavHost
import com.example.hundredplaces.ui.navigation.NavigationDestination
import com.example.hundredplaces.ui.navigation.NavigationDrawerContent
import com.example.hundredplaces.ui.places.PlacesDestination

@Composable
fun HomeScreen(
    startDestination: String,
    navController: NavHostController,
    navigationType: AppNavigationType,
    contentType: AppContentType,
    modifier: Modifier = Modifier
) {
    val navigationItemContentList = listOf(PlacesDestination, MapDestination, AchievementsDestination, AccountDestination)

    if (navigationType == AppNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(
                    modifier = Modifier.width(240.dp)
                ) {
                    NavigationDrawerContent(
                        navigationItemContentList = navigationItemContentList,
                        navController = navController,
                        modifier = modifier
                            .wrapContentWidth()
                            .fillMaxHeight()
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                }
            }) {
            HomeScreenContent(
                startDestination = startDestination,
                navController = navController,
                contentType = contentType,
                navigationType = navigationType,
                navigationItemContentList = navigationItemContentList)
        }
    }
    else {
        HomeScreenContent(
            startDestination = startDestination,
            navController = navController,
            contentType = contentType,
            navigationType = navigationType,
            navigationItemContentList = navigationItemContentList,
            modifier = modifier
        )
    }
}

@Composable
private fun HomeScreenContent(
    startDestination: String,
    navController: NavHostController,
    contentType: AppContentType,
    navigationType: AppNavigationType,
    navigationItemContentList: List<NavigationDestination>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(visible = navigationType == AppNavigationType.NAVIGATION_RAIL) {
                AppNavigationRail(
                    navigationItemContentList = navigationItemContentList,
                    navController = navController,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
            ) {
                HundredPlacesNavHost(
                    startDestination = startDestination,
                    navController = navController,
                    contentType = contentType,
                    modifier = Modifier.weight(1f)
                )
                AnimatedVisibility(visible = navigationType == AppNavigationType.BOTTOM_NAVIGATION) {
                    AppBottomNavigationBar(
                        navigationItemContentList = navigationItemContentList,
                        navController = navController,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}