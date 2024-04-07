package com.example.hundredplaces.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.AppBottomNavigationBar
import com.example.hundredplaces.ui.AppContentType
import com.example.hundredplaces.ui.AppNavigationRail
import com.example.hundredplaces.ui.AppNavigationType
import com.example.hundredplaces.ui.NavigationDrawerContent
import com.example.hundredplaces.ui.NavigationItemContent

@Composable
fun HomeScreen(
    navigationType: AppNavigationType,
    contentType: AppContentType,
    modifier: Modifier = Modifier
) {
    val navigationItemContentList = listOf(
        NavigationItemContent(
            icon = ImageVector.vectorResource(id = R.drawable.rounded_museum_24),
            text = stringResource(R.string.places)
        ),
        NavigationItemContent(
            icon = ImageVector.vectorResource(id = R.drawable.rounded_map_24),
            text = stringResource(R.string.map)
        ),
        NavigationItemContent(
            icon = ImageVector.vectorResource(id = R.drawable.rounded_trophy_24),
            text = stringResource(R.string.achievements)
        ),
        NavigationItemContent(
            icon = Icons.Default.AccountCircle,
            text = stringResource(R.string.account)
        )
    )

    if (navigationType == AppNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(
            drawerContent = {
                NavigationDrawerContent(
                    navigationItemContentList = navigationItemContentList,
                    modifier = modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                )
            }) {
            HomeScreenContent(
                contentType = contentType,
                navigationType = navigationType,
                navigationItemContentList = navigationItemContentList)
        }
    }
    else {
        HomeScreenContent(
            contentType = contentType,
            navigationType = navigationType,
            navigationItemContentList = navigationItemContentList,
            modifier = modifier
        )
    }
}

@Composable
private fun HomeScreenContent(
    contentType: AppContentType,
    navigationType: AppNavigationType,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(visible = navigationType == AppNavigationType.NAVIGATION_RAIL) {
                AppNavigationRail(
                    navigationItemContentList = navigationItemContentList
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
            ) {
                if (contentType == AppContentType.LIST_AND_DETAIL) {

                } else {

                }
                AnimatedVisibility(visible = navigationType == AppNavigationType.BOTTOM_NAVIGATION) {
                    AppBottomNavigationBar(
                        navigationItemContentList = navigationItemContentList,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
