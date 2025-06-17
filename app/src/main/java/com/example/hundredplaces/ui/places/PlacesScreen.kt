package com.example.hundredplaces.ui.places

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hundredplaces.R
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.ui.AppViewModelProvider
import com.example.hundredplaces.ui.navigation.MenuNavigationDestination
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsScreen
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsViewModel
import kotlinx.coroutines.launch

object PlacesDestination : MenuNavigationDestination {
    override val route = "Places"
    override val title = R.string.places
    override val iconRes = R.drawable.baseline_location_pin_24
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreenV2(
    onCameraButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    placesViewModel: PlacesViewModel = viewModel  (
            factory = AppViewModelProvider.Factory
            ),
) {
    val uiState = placesViewModel.uiState.collectAsStateWithLifecycle().value
    val navigator = rememberListDetailPaneScaffoldNavigator<Long>()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        placesViewModel.refresh()
    }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        modifier = modifier,
        listPane = {
            AnimatedPane {
                PlacesListContent(
                    uiState = uiState,
                    navigator = navigator,
                    onSearchTextChange = placesViewModel::onSearchTextChange,
                    onCameraButtonClick = onCameraButtonClick,
                    onFilterActionButtonClick = placesViewModel::openFilterScreen,
                    onFilterChipClick = placesViewModel::toggleFilter,
                    onRefresh = placesViewModel::refresh,
                    onItemClick = {
                        scope.launch {
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                it.place.id
                            )
                        }
                    },
                    saveRating = placesViewModel::saveRating,
                    toggleFavorite = placesViewModel::toggleFavorite
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val contentKey = navigator.currentDestination?.contentKey
                if (contentKey != null) {
                    val owner = LocalViewModelStoreOwner.current
                    val defaultExtras = (owner as? HasDefaultViewModelProviderFactory)?.defaultViewModelCreationExtras ?: CreationExtras.Empty

                    PlaceDetailsScreen(
                        navigateBack = { scope.launch { navigator.navigateBack() } },
                        isFullScreen = navigator.isListExpanded(),
                        addVisit = false,
                        placesDetailsViewModel = viewModel(
                            key = "$contentKey",
                            factory = AppViewModelProvider.Factory,
                            extras = MutableCreationExtras(defaultExtras).apply {
                                set(AppViewModelProvider.PLACE_ID_KEY, contentKey)
                            }
                        )
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = modifier
                            .fillMaxSize()
                            .padding(horizontal = dimensionResource(R.dimen.padding_small))
                    ) {
                        Text(
                            text = stringResource(R.string.select_place_see_details)
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlacesListContent(
    uiState: PlacesUiState,
    navigator: ThreePaneScaffoldNavigator<Long>,
    onSearchTextChange: (String) -> Unit,
    onCameraButtonClick: () -> Unit,
    onFilterActionButtonClick: () -> Unit,
    onFilterChipClick: (PlaceFiltersEnum) -> Unit,
    onRefresh: () -> Unit,
    onItemClick: (PlaceWithCityAndImages) -> Unit,
    saveRating: (Long, Double, Boolean) -> Unit,
    toggleFavorite: (Long, Double, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        Scaffold(
            topBar = {
                SearchBarWithCameraButton(
                    searchText = uiState.searchText,
                    onSearchTextChange = onSearchTextChange,
                    onCameraButtonClick = onCameraButtonClick
                )
            },
            floatingActionButton = {
                FilterButton(
                    filtersScreenAlignment = if (navigator.isDetailExpanded()) Alignment.BottomStart else Alignment.BottomEnd,
                    isFiltersScreenOpen = uiState.isFilterScreenOpen,
                    filtersSetSize = uiState.filtersSet.size,
                    onClick = onFilterActionButtonClick,
                ) {
                    PlacesFilterCategories(
                        onFilterChipClick = onFilterChipClick,
                        rangeSliderState = uiState.rangeSliderState,
                        filtersSet = uiState.filtersSet,
                        modifier = Modifier
                            .padding(52.dp)
                    )
                }
            },
            floatingActionButtonPosition = if (navigator.isDetailExpanded()) FabPosition.Start else FabPosition.End,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            modifier = modifier
        ) {
            val shimmer = shimmerBrush()
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium)),
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                if (uiState.isRefreshing) {
                    repeat(5) {
                        item(key = it) {
                            PlaceItemLoading(shimmer)
                        }
                    }
                }
                else {
                    items(uiState.filteredPlaces, key = { it.place.id }) {
                        PlaceItem(
                            placeWithCityAndImages = it,
                            userRating = uiState.ratings.getValue(it.place.id),
                            isFavorite = uiState.favorites.contains(it.place.id),
                            isSelected = navigator.currentDestination?.contentKey == it.place.id && navigator.isDetailExpanded(),
                            onClick = { onItemClick(it) },
                            saveRating = saveRating,
                            toggleFavorite = toggleFavorite,
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarWithCameraButton(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onCameraButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            expanded = false,
            onExpandedChange = {},
            windowInsets = WindowInsets(0, 0, 0, 0),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f }
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(R.dimen.padding_small),
                    horizontal = dimensionResource(R.dimen.padding_medium)
                ),
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchText,
                    onQueryChange = onSearchTextChange,
                    onSearch = onSearchTextChange,
                    expanded = false,
                    onExpandedChange = {},
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_for_a_place)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.round_search_24),
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            VerticalDivider(
                                color = Color.Gray,
                                thickness = 2.dp,
                                modifier = Modifier
                                    .height(32.dp)
                            )
                            IconButton(
                                onClick = onCameraButtonClick
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.round_photo_camera_24),
                                    contentDescription = stringResource(R.string.open_camera_to_search)
                                )
                            }
                        }
                    }
                )
            }
        ) { }
    }
}

/**
 * A button composable to be used as a floating action button in a [Scaffold].
 *
 * @param filtersScreenAlignment - the position of the [content] screen.
 * @param isFiltersScreenOpen - defines whether the content should be visible.
 * @param filtersSetSize - defines the number of filters that are selected and is shown in the [BadgedBox]'s [Badge]
 * @param onClick - called when this button is clicked.
 * @param modifier - the modifier to be applied to the layout.
 * @param content - the filter screen to appear or disappear based on the value of [isFiltersScreenOpen].
 */
@Composable
fun FilterButton(
    filtersScreenAlignment: Alignment,
    isFiltersScreenOpen: Boolean,
    filtersSetSize: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)
) {
    Box(
        contentAlignment = filtersScreenAlignment,
        modifier = modifier
    ) {
        AnimatedVisibility(
            visible = isFiltersScreenOpen
        ) {
            content()
        }
        BadgedBox(
            badge = {
                if (filtersSetSize > 0) {
                    Badge {
                        Text("$filtersSetSize")
                    }
                }
            }
        ) {
            FloatingActionButton (
                onClick = onClick,
            ) {
                Icon(
                    painter = painterResource(R.drawable.rounded_filter_alt_24),
                    contentDescription = stringResource(R.string.filter),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(top = 5.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlacesFilterCategories(
    onFilterChipClick: (PlaceFiltersEnum) -> Unit,
    rangeSliderState: RangeSliderState,
    filtersSet: Set<PlaceFiltersEnum>,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .width(325.dp)
            .height(425.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            item {
                Column {
                    val startInteractionSource = remember { MutableInteractionSource() }
                    val endInteractionSource = remember { MutableInteractionSource() }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            label = { Text(text = stringResource(R.string.rating_category_name)) },
                            selected = filtersSet.contains(PlaceFiltersEnum.RATING),
                            onClick = {},
                            modifier = Modifier
                                .padding(end = dimensionResource(R.dimen.padding_small))
                        )
                        RangeSlider(
                            state = rangeSliderState,
                            startThumb = {
                                SliderDefaults.Thumb(
                                    interactionSource = startInteractionSource,
                                    thumbSize = DpSize(4.dp, 24.dp)
                                )
                            },
                            endThumb = {
                                SliderDefaults.Thumb(
                                    interactionSource = endInteractionSource,
                                    thumbSize = DpSize(4.dp, 24.dp),
                                )
                            },
                            track = {
                                SliderDefaults.Track(
                                    rangeSliderState = it,
                                    thumbTrackGapSize = 4.dp,
                                    trackInsideCornerSize = 6.dp,
                                    colors = SliderDefaults.colors(
                                        activeTickColor = Color.Transparent,
                                        inactiveTickColor = Color.Transparent
                                    ),
                                    drawStopIndicator = {} //Removes the dots at the start and the end of the track
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                    Text(
                        text = stringResource(R.string.range_format).format(
                            rangeSliderState.activeRangeStart,
                            rangeSliderState.activeRangeEnd
                        )
                    )
                }
            }
            item {
                HorizontalDivider(
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.padding_small))
                )
            }
            item {
                FlowRow {
                    for (filter in PlaceFiltersEnum.entries.filterNot { filter -> filter == PlaceFiltersEnum.RATING }) {
                        FilterChip(
                            onClick = { onFilterChipClick(filter) },
                            label = { Text(text = stringResource(filter.categoryName)) },
                            selected = filtersSet.contains(filter),
                            leadingIcon = {
                                AnimatedVisibility(
                                    filtersSet.contains(filter)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.round_done_24),
                                        contentDescription = null
                                    )
                                }
                            },
                            modifier = Modifier
                                .padding(end = dimensionResource(R.dimen.padding_small))
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(
    selectAndNavigateToPlace: (Long) -> Unit,
    selectPlace: (Long) -> Unit,
    contentType: PlacesScreenContentType,
    placesDetailsViewModel: PlaceDetailsViewModel,
    placesViewModel: PlacesViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = placesViewModel.uiState.collectAsStateWithLifecycle().value

    Scaffold(
        floatingActionButton = {
                FilterButton(
                    filtersScreenAlignment = if (contentType == PlacesScreenContentType.LIST_ONLY) Alignment.BottomEnd else Alignment.BottomStart,
                    isFiltersScreenOpen = uiState.isFilterScreenOpen,
                    filtersSetSize = uiState.filtersSet.size,
                    onClick = placesViewModel::openFilterScreen,
                ) {
                    PlacesFilterCategories(
                        onFilterChipClick = placesViewModel::toggleFilter,
                        rangeSliderState = uiState.rangeSliderState,
                        filtersSet = uiState.filtersSet,
                        modifier = Modifier
                            .padding(48.dp)
                    )
                }
            },
        floatingActionButtonPosition = if (contentType == PlacesScreenContentType.LIST_ONLY) FabPosition.End else FabPosition.Start,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier
    ) {
        if (contentType == PlacesScreenContentType.LIST_ONLY) {
            PlacesListOnlyContent(
                placesUiState = uiState,
                cardOnClick = selectAndNavigateToPlace,
                saveRating = placesViewModel::saveRating,
                toggleFavorite = placesViewModel::toggleFavorite,
                modifier = Modifier
                    .padding(it)
                    .fillMaxHeight()
            )
        } else {
            PlacesListAndDetailsContent(
                placesDetailsViewModel = placesDetailsViewModel,
                placesUiState = uiState,
                cardOnClick = selectPlace,
                saveRating = placesViewModel::saveRating,
                toggleFavorite = placesViewModel::toggleFavorite,
                modifier = Modifier
                    .padding(it)
                    .fillMaxHeight()
            )
        }
    }
}


@Composable
private fun PlacesListOnlyContent(
    placesUiState: PlacesUiState,
    cardOnClick: (Long) -> Unit,
    saveRating: (Long, Double, Boolean) -> Unit,
    toggleFavorite: (placeId: Long, rating: Double, isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_small)),
        modifier = modifier
    ) {
        items(placesUiState.filteredPlaces, key = { it.place.id }) {
            PlaceItem(
                placeWithCityAndImages = it,
                userRating = 0.0,
                isFavorite = placesUiState.favorites.contains(it.place.id),
                isSelected = false,
                onClick = { cardOnClick(it.place.id) },
                saveRating = saveRating,
                toggleFavorite = toggleFavorite,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PlacesListAndDetailsContent(
    placesDetailsViewModel: PlaceDetailsViewModel,
    placesUiState: PlacesUiState,
    cardOnClick: (Long) -> Unit,
    saveRating: (Long, Double, Boolean) -> Unit,
    toggleFavorite: (Long, Double, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(
                    end = 0.dp,
                    top = dimensionResource(id = R.dimen.padding_small),
                    start = dimensionResource(id = R.dimen.padding_small)
                )
        ) {
            items(
                placesUiState.filteredPlaces,
                key = { it.place.id }) {
                PlaceItem(
                    placeWithCityAndImages = it,
                    userRating = 0.0,
                    isFavorite = placesUiState.favorites.contains(it.place.id),
                    isSelected = false,
                    onClick = { cardOnClick(it.place.id) },
                    saveRating = saveRating,
                    toggleFavorite = toggleFavorite,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .fillMaxWidth()
                )
            }
        }
        val activity = LocalActivity.current
        PlaceDetailsScreen(
            navigateBack = { activity?.finish() },
            isFullScreen = true,
            addVisit = false,
            placesDetailsViewModel = placesDetailsViewModel,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun ThreePaneScaffoldNavigator<*>.isListExpanded() =
    scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun ThreePaneScaffoldNavigator<*>.isDetailExpanded() =
    scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded