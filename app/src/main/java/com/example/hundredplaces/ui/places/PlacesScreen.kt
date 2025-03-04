package com.example.hundredplaces.ui.places

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.navigation.MenuNavigationDestination
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsScreen
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsViewModel

object PlacesDestination : MenuNavigationDestination {
    override val route = "Places"
    override val title = R.string.places
    override val iconRes = R.drawable.rounded_museum_24
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreenV2(
    placesDetailsViewModel: PlaceDetailsViewModel,
    placesViewModel: PlacesViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = placesViewModel.uiState.collectAsStateWithLifecycle().value
    val navigator = rememberListDetailPaneScaffoldNavigator<Long>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                Scaffold(
                    floatingActionButton = {
                        Box(
                            contentAlignment = if (navigator.isDetailExpanded()) Alignment.BottomStart else Alignment.BottomEnd
                        ) {
                            AnimatedVisibility(
                                visible = uiState.isFilterScreenOpen
                            ) {
                                PlacesFilterCategories(
                                    filterOnClick = { filter ->
                                        placesViewModel.toggleFilter(filter)
                                                    },
                                    rangeSliderState = uiState.rangeSliderState,
                                    filtersSet = uiState.filtersSet,
                                    modifier = Modifier
                                        .padding(48.dp)
                                )
                            }
                            BadgedBox(
                                badge = {
                                    if (uiState.filtersSet.isNotEmpty()) {
                                        Badge {
                                            Text("${uiState.filtersSet.size}")
                                        }
                                    }
                                }
                            ) {
                                IconButton(
                                    onClick = placesViewModel::openFilterScreen,
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                                    ),
                                    modifier = Modifier
                                        .size(48.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.rounded_filter_alt_24),
                                        contentDescription = stringResource(R.string.filter),
                                        modifier = Modifier
                                            .size(42.dp)
                                            .padding(top = 5.dp)
                                    )
                                }
                            }
                        }
                    },
                    floatingActionButtonPosition = if (navigator.isDetailExpanded()) FabPosition.Start else FabPosition.End,
                    contentWindowInsets = WindowInsets(0,0,0,0),
                    modifier = modifier
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_small)),
                        modifier = Modifier
                            .padding(it)
                            .fillMaxHeight()
                    ) {
                        items(uiState.filteredPlaces, key = { it.place.id }) {
                            PlaceItem(
                                placeWithCityAndImages = it,
                                rating = 0.0,
                                isFavorite = uiState.favorites.contains(it.place.id),
                                isSelected = navigator.currentDestination?.content == it.place.id && navigator.isDetailExpanded(),
                                distance = uiState.distances[it.place.id],
                                onClick = {
                                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it.place.id)
                                    placesDetailsViewModel.setPlaceId(it.place.id)
                                },
                                toggleFavorite = placesViewModel::toggleFavorite,
                                modifier = Modifier
                                    .padding(dimensionResource(id = R.dimen.padding_small))
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let {
                    PlaceDetailsScreen(
                        navigateBack = { navigator.navigateBack() },
                        isFullScreen = navigator.isListExpanded(),
                        placesDetailsViewModel = placesDetailsViewModel,
                        modifier = modifier
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlacesFilterCategories(
    filterOnClick: (PlaceFiltersEnum) -> Unit,
    rangeSliderState: RangeSliderState,
    filtersSet: Set<PlaceFiltersEnum>,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .width(325.dp)
            .height(500.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        ){
            item {
                Column {
                    val startInteractionSource = remember { MutableInteractionSource() }
                    val endInteractionSource = remember { MutableInteractionSource() }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val color by animateColorAsState(
                            targetValue = if (filtersSet.contains(PlaceFiltersEnum.RATING)) Color(
                                3,
                                100,
                                252
                            ) else Color.LightGray,
                            label = "Category color Animation"
                        )
                        OutlinedCard(
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, color),
                            modifier = Modifier
                                .padding(end = dimensionResource(R.dimen.padding_small))
                        ) {
                            Text(
                                text = stringResource(R.string.rating_category_name),
                                color = color,
                                modifier = Modifier
                                    .padding(dimensionResource(R.dimen.padding_small))
                            )
                        }
                        RangeSlider(
                            state = rangeSliderState,
                            startThumb = {
                                SliderDefaults.Thumb(
                                    interactionSource = startInteractionSource,
                                    thumbSize = DpSize(4.dp, 32.dp)
                                )
                            },
                            endThumb = {
                                SliderDefaults.Thumb(
                                    interactionSource = endInteractionSource,
                                    thumbSize = DpSize(4.dp, 32.dp),
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
                        val color by animateColorAsState(
                            targetValue = if (filtersSet.contains(filter)) Color(
                                3,
                                100,
                                252
                            ) else Color.LightGray,
                            label = "Category color Animation"
                        )
                        OutlinedCard(
                            shape = RoundedCornerShape(8.dp),
                            onClick = { filterOnClick(filter) },
                            border = BorderStroke(1.dp, color),
                            modifier = Modifier
                                .padding(end = dimensionResource(R.dimen.padding_small))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AnimatedVisibility(
                                    filtersSet.contains(filter)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.round_done_24),
                                        contentDescription = null,
                                        tint = Color(3, 100, 252),
                                        modifier = Modifier
                                            .padding(start = dimensionResource(R.dimen.padding_small))
                                    )
                                }
                                Text(
                                    text = stringResource(filter.categoryName),
                                    color = color,
                                    modifier = Modifier
                                        .padding(dimensionResource(R.dimen.padding_small))
                                )
                            }
                        }
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
            Box(
                contentAlignment = if (contentType == PlacesScreenContentType.LIST_ONLY) Alignment.BottomEnd else Alignment.BottomStart
            ) {
                AnimatedVisibility(
                    visible = uiState.isFilterScreenOpen
                ) {
                    PlacesFilterCategories(
                        filterOnClick = { filter ->
                            placesViewModel.toggleFilter(filter)
                                        },
                        rangeSliderState = uiState.rangeSliderState,
                        filtersSet = uiState.filtersSet,
                        modifier = Modifier
                            .padding(48.dp)
                    )
                }
                BadgedBox(
                    badge = {
                        if (uiState.filtersSet.isNotEmpty()) {
                            Badge {
                                Text("${uiState.filtersSet.size}")
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = placesViewModel::openFilterScreen,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        ),
                        modifier = Modifier
                            .size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.rounded_filter_alt_24),
                            contentDescription = stringResource(R.string.filter),
                            modifier = Modifier
                                .size(42.dp)
                                .padding(top = 5.dp)
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = if (contentType == PlacesScreenContentType.LIST_ONLY) FabPosition.End else FabPosition.Start,
        contentWindowInsets = WindowInsets(0,0,0,0),
        modifier = modifier
    ) {
        if (contentType == PlacesScreenContentType.LIST_ONLY) {
            PlacesListOnlyContent(
                placesUiState = uiState,
                cardOnClick = selectAndNavigateToPlace,
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
                rating = 0.0,
                isFavorite = placesUiState.favorites.contains(it.place.id),
                isSelected = false,
                distance = placesUiState.distances[it.place.id],
                onClick = { cardOnClick(it.place.id) },
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
    toggleFavorite: (placeId: Long, rating: Double, isFavorite: Boolean) -> Unit,
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
                    rating = 0.0,
                    isFavorite = placesUiState.favorites.contains(it.place.id),
                    isSelected = placesDetailsViewModel.getPlaceId() == it.place.id,
                    distance = placesUiState.distances[it.place.id],
                    onClick = { cardOnClick(it.place.id) },
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
            placesDetailsViewModel = placesDetailsViewModel,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun ThreePaneScaffoldNavigator<*>.isListExpanded() = scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun ThreePaneScaffoldNavigator<*>.isDetailExpanded() = scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded