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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.AppContentType
import com.example.hundredplaces.ui.AppNavigationType
import com.example.hundredplaces.ui.AppViewModelProvider
import com.example.hundredplaces.ui.account.AccountDestination
import com.example.hundredplaces.ui.account.AccountUiState
import com.example.hundredplaces.ui.account.AccountViewModel
import com.example.hundredplaces.ui.achievements.AchievementsDestination
import com.example.hundredplaces.ui.map.MapDestination
import com.example.hundredplaces.ui.navigation.AppBottomNavigationBar
import com.example.hundredplaces.ui.navigation.AppNavigationRail
import com.example.hundredplaces.ui.navigation.HundredPlacesNavHost
import com.example.hundredplaces.ui.navigation.MenuNavigationDestination
import com.example.hundredplaces.ui.navigation.NavigationDrawerContent
import com.example.hundredplaces.ui.places.PlacesDestination

@Composable
fun HomeScreen(
    startDestination: String,
    navController: NavHostController,
    navigationType: AppNavigationType,
    contentType: AppContentType,
    modifier: Modifier = Modifier,
    sharedAccountViewModel: AccountViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val navigationItemContentList = listOf(PlacesDestination, MapDestination, AchievementsDestination, AccountDestination)
    val sharedAccountUiState = sharedAccountViewModel.uiState.collectAsState()

    if (navigationType == AppNavigationType.PERMANENT_NAVIGATION_DRAWER && sharedAccountUiState.value.isLoggedIn) {
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
                sharedAccountUiState = sharedAccountUiState.value,
                sharedAccountViewModel = sharedAccountViewModel,
                startDestination = startDestination,
                navController = navController,
                contentType = contentType,
                navigationType = navigationType,
                navigationItemContentList = navigationItemContentList)
        }
    }
    else {
        HomeScreenContent(
            sharedAccountUiState = sharedAccountUiState.value,
            sharedAccountViewModel = sharedAccountViewModel,
            startDestination = startDestination,
            navController = navController,
            contentType = contentType,
            navigationType = navigationType,
            navigationItemContentList = navigationItemContentList,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    sharedAccountUiState: AccountUiState,
    sharedAccountViewModel: AccountViewModel,
    startDestination: String,
    navController: NavHostController,
    contentType: AppContentType,
    navigationType: AppNavigationType,
    navigationItemContentList: List<MenuNavigationDestination>,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.displayLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        modifier = modifier
    ) {paddingValues ->
        Box(
            modifier = Modifier
            .padding(paddingValues)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(visible = navigationType == AppNavigationType.NAVIGATION_RAIL && sharedAccountUiState.isLoggedIn) {
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
                        sharedAccountViewModel = sharedAccountViewModel,
                        sharedAccountUiState = sharedAccountUiState,
                        modifier = Modifier.weight(1f)
                    )
                    AnimatedVisibility(visible = navigationType == AppNavigationType.BOTTOM_NAVIGATION && sharedAccountUiState.isLoggedIn) {
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
}