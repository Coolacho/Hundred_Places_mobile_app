package com.example.hundredplaces.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.AppContentType
import com.example.hundredplaces.ui.AppNavigationType
import com.example.hundredplaces.ui.AppViewModelProvider
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
    navController: NavHostController,
    navigationType: AppNavigationType,
    contentType: AppContentType,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    val navigationItemContentList = listOf(PlacesDestination, MapDestination, AchievementsDestination, AccountDestination)

    if (navigationType == AppNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(
                    modifier = Modifier.width(240.dp)
                ) {
                    NavigationDrawerContent(
                        currentDestination = navController.currentDestination?.route ?: "",
                        navigationItemContentList = navigationItemContentList,
                        onTabPressed = {},
                        modifier = modifier
                            .wrapContentWidth()
                            .fillMaxHeight()
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                }
            }) {
            HomeScreenContent(
                uiState = uiState,
                viewModel = viewModel,
                navController = navController,
                contentType = contentType,
                navigationType = navigationType,
                navigationItemContentList = navigationItemContentList)
        }
    }
    else {
        HomeScreenContent(
            uiState = uiState,
            viewModel = viewModel,
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
    viewModel: HomeViewModel,
    uiState: HomeUiState,
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
                    currentDestination = navController.currentDestination?.route ?: "",
                    navigationItemContentList = navigationItemContentList,
                    onTabPressed = { }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
            ) {
                HundredPlacesTopBar(
                    title = navController.currentDestination?.route ?: "",
                    uiState = uiState,
                    canNavigateBack = true,
                    navigateUp = { /*TODO*/ },
                    canChangeLayout = true,
                    selectLayout = viewModel::selectLayout
                )
                HundredPlacesNavHost(navController = navController)
                AnimatedVisibility(visible = navigationType == AppNavigationType.BOTTOM_NAVIGATION) {
                    AppBottomNavigationBar(
                        currentDestination = navController.currentDestination?.route ?: "",
                        navigationItemContentList = navigationItemContentList,
                        onTabPressed = {},
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HundredPlacesTopBar(
    title: String,
    uiState: HomeUiState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    canChangeLayout: Boolean,
    selectLayout: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title
                    )
                }
            },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (canChangeLayout) {
                IconButton(onClick = { selectLayout(!uiState.isLinearLayout) }) {
                    Icon(
                        painter = painterResource(id = uiState.toggleIcon),
                        contentDescription = stringResource(id = uiState.toggleContentDescription)
                    )
                }
            }
        },
        modifier = modifier
    )
    
}