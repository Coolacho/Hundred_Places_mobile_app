package com.example.hundredplaces.ui.places.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.navigation.NavigationDestination
import com.example.hundredplaces.ui.places.PlaceRating
import com.example.hundredplaces.ui.theme.HundredPlacesTheme

object PlaceDetailsDestination : NavigationDestination {
    override val route = "Place Details"
    override val iconRes: Int
        get() = TODO("Not yet implemented")
    const val PLACE_ID_ARG = "placeId"
    val routeWithArgs = "$route/{$PLACE_ID_ARG}"
}

@Composable
fun PlaceDetailsScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
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
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            ImageCarousel(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Row(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "Национален исторически музей",
                        fontSize = 24.sp,
                    )
                    Text(
                        text = "гр. София",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                PlaceRating(placeRating = 4.7)
            }
            Text(
                text = "Information",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            )
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat." +
                        "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur." +
                        "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(
                        horizontal = 32.dp
                    )

            )
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    modifier: Modifier = Modifier,
    images: List<Painter> = listOf(
        painterResource(id = R.drawable.nim),
        painterResource(id = R.drawable.exposision),
        painterResource(id = R.drawable.treasure)
    ) //TODO swap with place images and async image
) {
    Box(modifier = modifier){
        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = {images.size}
        )
        HorizontalPager(
            state = pagerState,
            pageSpacing = 48.dp,
            modifier = Modifier
        ) { page ->
            Card {
                Image(
                    painter = images[page],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
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
                        .size(16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ImageCarouselPreview() {
    HundredPlacesTheme {
        Surface {
            ImageCarousel()
        }
    }
}

@Preview
@Composable
private fun PlaceDetailsScreenPreview() {
    HundredPlacesTheme {
        Surface {
            PlaceDetailsScreen({})
        }
    }
}