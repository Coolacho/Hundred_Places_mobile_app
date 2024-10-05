package com.example.hundredplaces.ui.places

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hundredplaces.R
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.ui.account.AccountUiState
import com.example.hundredplaces.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.time.LocalDateTime

object PlaceDetailsDestination : NavigationDestination {
    override val route = "Place Details"
    const val PLACE_ID_ARG = "placeId"
    val routeWithArgs = "$route/{$PLACE_ID_ARG}"
}

val tabs = listOf(R.string.information, R.string.visits)

@Composable
fun PlaceDetailsScreen(
    navigateBack: () -> Unit,
    isFullScreen: Boolean,
    viewModel: PlacesViewModel,
    accountUiState: AccountUiState,
    placesUiState: PlacesUiState,
    modifier: Modifier = Modifier
) {
    if (placesUiState.selectedPlaceId == 0L)
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.select_place_see_details)
            )
        }
    }
    else
    {
        val selectedPlace: PlaceWithCityAndImages = viewModel.getSelectedPlace()
        Scaffold(
            floatingActionButton = {
                Button(
                    onClick = { viewModel.addVisit(accountUiState.currentUser) },
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.padding_medium),
                            vertical = dimensionResource(id = R.dimen.padding_small)
                        )
                ) {
                    Text(text = stringResource(R.string.take_your_badge))
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                if (!isFullScreen) {
                    IconButton(
                        onClick = navigateBack,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .zIndex(3f)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    ImageCarousel(
                        images = selectedPlace.images,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .padding(
                                bottom = dimensionResource(id = R.dimen.padding_medium)
                            )
                    )
                    if (!isFullScreen) {
                        Row {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = selectedPlace.place.name,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = selectedPlace.city,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            PlaceRating(placeRating = selectedPlace.place.rating)
                        }
                    }

                    val pagerState = rememberPagerState(
                        initialPage = 0,
                        pageCount = { 2 }
                    )
                    val coroutineScope = rememberCoroutineScope()

                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small))
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                text = {
                                    Text(
                                        text = stringResource(id = title),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                },
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                                }
                            )
                        }
                    }

                    HorizontalPager(
                        state = pagerState,
                        pageSpacing = 48.dp,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .height(340.dp)
                            .fillMaxWidth()
                            .padding(
                                horizontal = 32.dp,
                                vertical = dimensionResource(R.dimen.padding_small)
                            )
                    ) {
                        page ->
                        when(page){
                            0 -> InformationTab()
                            1 -> {viewModel.getVisitsByUserAndPlace(accountUiState.currentUser); VisitsTab(placesUiState.visits)}
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ImageCarousel(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { images.size }
        )
        HorizontalPager(
            state = pagerState,
            pageSpacing = 48.dp,
            modifier = Modifier
                .clip(CardDefaults.shape)
        ) { page ->
            Card {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(images[page])
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_broken_image),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray
                else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
fun InformationTab(
    modifier: Modifier = Modifier
)
{
    Text(
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat." +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur." +
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        textAlign = TextAlign.Justify,
        modifier = modifier
            .verticalScroll(rememberScrollState())
    )
}

@Composable
fun VisitsTab(
    visits: List<LocalDateTime>,
    modifier: Modifier = Modifier
)
{
    LazyColumn(
        modifier = modifier
    ) {
        if (visits.isEmpty())
        {
            item {
                Text(text = stringResource(R.string.place_not_visited))
            }
        }
        else
        {
            items(visits, key = { visits.indexOf(it) }) {
                Text(text = it.toString())
            }
        }
    }
}