package com.example.hundredplaces.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.hundredplaces.R

@Composable
fun AppBottomNavigationBar(
    currentDestination: String,
    navigationItemContentList: List<NavigationDestination>,
    onTabPressed: (NavigationDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentDestination == navItem.route,
                onClick = { },
                icon = {
                    Icon(
                        painter = painterResource(navItem.iconRes),
                        contentDescription = navItem.route
                    )
                }
            )
        }
    }
}

@Composable
fun AppNavigationRail(
    currentDestination: String,
    navigationItemContentList: List<NavigationDestination>,
    onTabPressed: (NavigationDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationRailItem(
                selected = currentDestination == navItem.route,
                onClick = {  },
                icon = {
                    Icon(
                        painter = painterResource(navItem.iconRes),
                        contentDescription = navItem.route
                    )
                }
            )
        }
    }
}

@Composable
fun NavigationDrawerContent(
    currentDestination: String,
    navigationItemContentList: List<NavigationDestination>,
    onTabPressed: (NavigationDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationDrawerItem(
                selected = currentDestination == navItem.route,
                label = {
                    Text(
                        text = navItem.route,
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(navItem.iconRes),
                        contentDescription = navItem.route
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent
                ),
                onClick = {  }
            )
        }
    }
}