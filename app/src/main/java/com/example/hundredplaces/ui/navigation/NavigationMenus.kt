package com.example.hundredplaces.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.account.AccountDestination
import com.example.hundredplaces.ui.achievements.AchievementsDestination
import com.example.hundredplaces.ui.camera.CameraScreenDestination
import com.example.hundredplaces.ui.camera.CameraUseCaseEnum
import com.example.hundredplaces.ui.map.MapDestination
import com.example.hundredplaces.ui.places.PlacesDestination

val navigationDestinationList = listOf<MenuNavigationDestination>(PlacesDestination, MapDestination, CameraScreenDestination, AchievementsDestination, AccountDestination)

@Composable
fun AppBottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(
        modifier = modifier
    ) {
        for (navDestination in navigationDestinationList) {
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == navDestination.route } == true,
                onClick = {
                    val route = when (navDestination)
                    {
                        CameraScreenDestination -> "${CameraScreenDestination.route}/${CameraUseCaseEnum.QR_CODE.name}"
                        else -> navDestination.route
                    }
                    navController.navigate(route){
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(navDestination.iconRes),
                        contentDescription = stringResource(id = navDestination.title)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = navDestination.title),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
            )
        }
    }
}

@Composable
fun AppNavigationRail(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationRail(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        for (navDestination in navigationDestinationList) {
            NavigationRailItem(
                selected = currentDestination?.hierarchy?.any { it.route == navDestination.route } == true,
                onClick = {
                    val route = when (navDestination)
                    {
                        CameraScreenDestination -> "${CameraScreenDestination.route}/${CameraUseCaseEnum.QR_CODE.name}"
                        else -> navDestination.route
                    }
                    navController.navigate(route){
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(navDestination.iconRes),
                        contentDescription = stringResource(navDestination.title)
                    )
                },
                label = {
                    Text(
                        text = stringResource(navDestination.title)
                    )
                }
            )
        }
    }
}

@Composable
fun NavigationDrawerContent(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_small))
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                )
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .padding(
                        top = 40.dp,
                        bottom = dimensionResource(R.dimen.padding_small),
                        start = dimensionResource(R.dimen.padding_small),
                        end = dimensionResource(R.dimen.padding_small))
            )
        }
        for (navDestination in navigationDestinationList) {
            NavigationDrawerItem(
                selected = currentDestination?.hierarchy?.any { it.route == navDestination.route } == true,
                onClick = {
                    val route = when (navDestination)
                    {
                        CameraScreenDestination -> "${CameraScreenDestination.route}/${CameraUseCaseEnum.QR_CODE.name}"
                        else -> navDestination.route
                    }
                    navController.navigate(route){
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                },
                label = {
                    Text(
                        text = stringResource(navDestination.title),
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(navDestination.iconRes),
                        contentDescription = stringResource(navDestination.title)
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
            )
        }
    }
}