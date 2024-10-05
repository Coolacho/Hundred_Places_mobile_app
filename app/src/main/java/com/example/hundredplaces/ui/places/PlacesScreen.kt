package com.example.hundredplaces.ui.places

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.account.AccountUiState
import com.example.hundredplaces.ui.navigation.MenuNavigationDestination

object PlacesDestination : MenuNavigationDestination {
    override val route = "Places"
    override val title = R.string.places
    override val iconRes = R.drawable.rounded_museum_24
}

/**
 * Entry route for Places screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(
    navigateToPlaceEntry: (Long) -> Unit,
    contentType: PlacesScreenContentType,
    accountUiState: AccountUiState,
    placesViewModel: PlacesViewModel,
    placesUiState: PlacesUiState,
    modifier: Modifier = Modifier
) {
    var isCategoryScreenOpened by remember { mutableStateOf(false) }
    val filterList = remember { mutableStateListOf<PlaceFilterCategoriesEnum>() }
    val rangeSliderState = remember {
        RangeSliderState(
            activeRangeStart = 0f,
            activeRangeEnd = 5f,
            steps = 49,
            valueRange = 0f..5f
        )
    }
    rangeSliderState.onValueChangeFinished = {
        if (rangeSliderState.activeRangeStart > 0f || rangeSliderState.activeRangeEnd < 5f)
        {
            if (!filterList.contains(PlaceFilterCategoriesEnum.RATING))
                filterList.add(PlaceFilterCategoriesEnum.RATING)
        }
        else filterList.remove(PlaceFilterCategoriesEnum.RATING)
        placesViewModel.filterPlaces(filterList, rangeSliderState.activeRangeStart, rangeSliderState.activeRangeEnd)
    }
    Scaffold(
        floatingActionButton = {
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                AnimatedVisibility(
                    visible = isCategoryScreenOpened
                ) {
                    PlacesFilterCategories(
                        placesViewModel = placesViewModel,
                        rangeSliderState = rangeSliderState,
                        filterList = filterList,
                        modifier = Modifier
                            .padding(48.dp)
                    )
                }
                BadgedBox(
                    badge = {
                        if (filterList.isNotEmpty()) {
                            Badge {
                                Text("${filterList.size}")
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = { isCategoryScreenOpened = !isCategoryScreenOpened },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
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
        modifier = modifier
    ) {
        if (contentType == PlacesScreenContentType.LIST_ONLY) {
            PlacesListOnlyContent(
                placesUiState = placesUiState,
                cardOnClick = placesViewModel::selectPlace,
                navigateToPlaceEntry = navigateToPlaceEntry,
                modifier = Modifier
                    .padding(it)
            )
        } else {
            PlacesListAndDetailsContent(
                accountUiState = accountUiState,
                viewModel = placesViewModel,
                placesUiState = placesUiState,
                cardOnClick = placesViewModel::selectPlace,
                modifier = Modifier
                    .padding(it)
            )
        }
    }
}

@Composable
private fun PlacesListOnlyContent(
    placesUiState: PlacesUiState,
    cardOnClick: (Long) -> Unit,
    navigateToPlaceEntry: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        items(placesUiState.filteredPlaces, key = { it.place.id }) {
            PlaceItem(
                placeWithCityAndImages = it,
                isSelected = false,
                distance = placesUiState.distances[it.place.id],
                onClick = {
                    cardOnClick(it.place.id)
                    navigateToPlaceEntry(it.place.id)
                },
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PlacesListAndDetailsContent(
    viewModel: PlacesViewModel,
    accountUiState: AccountUiState,
    placesUiState: PlacesUiState,
    cardOnClick: (Long) -> Unit,
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
                    isSelected = placesUiState.selectedPlaceId == it.place.id,
                    distance = placesUiState.distances[it.place.id],
                    onClick = { cardOnClick(it.place.id) },
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .fillMaxWidth()
                )
            }
        }
        val activity = LocalContext.current as Activity
        PlaceDetailsScreen(
            navigateBack = { activity.finish() },
            isFullScreen = true,
            accountUiState = accountUiState,
            viewModel = viewModel,
            placesUiState = placesUiState,
            modifier = Modifier
                .weight(1f)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlacesFilterCategories(
    placesViewModel: PlacesViewModel,
    rangeSliderState: RangeSliderState,
    filterList: MutableList<PlaceFilterCategoriesEnum>,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .width(325.dp)
            .height(500.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            val startInteractionSource = remember { MutableInteractionSource() }
            val endInteractionSource = remember { MutableInteractionSource() }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedCard(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(end = dimensionResource(R.dimen.padding_small))
                ) {
                    Text(
                        text = stringResource(R.string.rating_category_name),
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                }
                RangeSlider(
                    state = rangeSliderState,
                    startThumb = {
                        SliderDefaults.Thumb(
                            interactionSource = startInteractionSource,
                            thumbSize = _root_ide_package_.androidx.compose.ui.unit.DpSize(4.dp, 32.dp)
                        )
                    },
                    endThumb = {
                        SliderDefaults.Thumb(
                            interactionSource = endInteractionSource,
                            thumbSize = _root_ide_package_.androidx.compose.ui.unit.DpSize(4.dp, 32.dp),
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
                            drawStopIndicator  = {} //Removes the dots at the start and the end of the track
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                )
            }
            Text(text = stringResource(R.string.range_format).format(rangeSliderState.activeRangeStart, rangeSliderState.activeRangeEnd))
        }
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_small))
        )
        FlowRow(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            for (category in PlaceFilterCategoriesEnum.entries.filterNot { category -> category == PlaceFilterCategoriesEnum.RATING }) {
                val color by animateColorAsState(
                    targetValue = if (filterList.contains(category)) Color(3, 100, 252) else Color.LightGray,
                    label = "Category color Animation"
                )
                OutlinedCard(
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        if (filterList.contains(category))
                            filterList.remove(category)
                        else filterList.add(category)
                        placesViewModel.filterPlaces(filterList, rangeSliderState.activeRangeStart, rangeSliderState.activeRangeEnd)
                    },
                    border = BorderStroke(1.dp, color),
                    modifier = Modifier
                        .padding(end = dimensionResource(R.dimen.padding_small))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedVisibility(
                            filterList.contains(category)
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
                            text = stringResource(category.categoryName),
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