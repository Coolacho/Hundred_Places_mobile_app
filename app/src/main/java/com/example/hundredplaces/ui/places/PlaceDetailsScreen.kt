package com.example.hundredplaces.ui.places

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.example.hundredplaces.ui.account.AccountUiState
import com.example.hundredplaces.ui.navigation.NavigationDestination
import java.time.LocalDateTime

object PlaceDetailsDestination : NavigationDestination {
    override val route = "Place Details"
    const val PLACE_ID_ARG = "placeId"
    val routeWithArgs = "$route/{$PLACE_ID_ARG}"
}

val tabs = listOf(R.string.information, R.string.visits)
const val CONTENT_ANIMATION_DURATION = 300

@Composable
fun PlaceDetailsScreen(
    navigateBack: () -> Unit,
    isFullScreen: Boolean,
    viewModel: PlacesViewModel,
    accountUiState: AccountUiState,
    placesUiState: PlacesUiState,
    modifier: Modifier = Modifier
) {
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
                    images = placesUiState.selectedPlace.images,
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
                                text = placesUiState.selectedPlace.place.name,
                                fontSize = 24.sp
                            )
                            Text(
                                text = placesUiState.selectedPlace.city,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        PlaceRating(placeRating = placesUiState.selectedPlace.place.rating)
                    }
                }


                TabRow(
                    selectedTabIndex = placesUiState.selectedDetailsTab,
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
                            selected = placesUiState.selectedDetailsTab == index,
                            onClick = { viewModel.selectDetailsTab(index) }
                        )
                    }
                }
                AnimatedContent(
                    targetState = placesUiState.selectedDetailsTab,
                    label = "TabAnimation",
                    modifier = Modifier
                        .height(340.dp)
                        .fillMaxWidth()
                        .padding(
                            horizontal = 32.dp,
                            vertical = dimensionResource(id = R.dimen.padding_small)
                        ),
                    transitionSpec = {
                        if (targetState > initialState) {
                            // Going forwards in the survey: Set the initial offset to start
                            // at the size of the content so it slides in from right to left, and
                            // slides out from the left of the screen to -fullWidth
                            slideInHorizontally(
                                animationSpec = tween(CONTENT_ANIMATION_DURATION),
                                initialOffsetX = { fullWidth -> fullWidth }
                            ) togetherWith
                                    slideOutHorizontally(
                                        animationSpec = tween(CONTENT_ANIMATION_DURATION),
                                        targetOffsetX = { fullWidth -> -fullWidth }
                                    )
                        } else {
                            // Going back to the previous question in the set, we do the same
                            // transition as above, but with different offsets - the inverse of
                            // above, negative fullWidth to enter, and fullWidth to exit.
                            slideInHorizontally(
                                animationSpec = tween(CONTENT_ANIMATION_DURATION),
                                initialOffsetX = { fullWidth -> -fullWidth }
                            ) togetherWith
                                    slideOutHorizontally(
                                        animationSpec = tween(CONTENT_ANIMATION_DURATION),
                                        targetOffsetX = { fullWidth -> fullWidth }
                                    )
                        }
                    }
                ) { targetState ->
                    when(targetState){
                        0 -> InformationTab()
                        1 -> {viewModel.getVisitsByUserAndPlace(accountUiState.currentUser); VisitsTab(placesUiState.visits)}
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
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
        items(visits, key = { visits.indexOf(it) }) {
            Text(text = it.toString())
        }
    }
}