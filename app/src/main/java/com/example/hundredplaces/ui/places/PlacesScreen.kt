package com.example.hundredplaces.ui.places

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hundredplaces.R
import com.example.hundredplaces.data.FakePlaceDataSource
import com.example.hundredplaces.data.model.place.PlaceTypeEnum
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.ui.AppContentType
import com.example.hundredplaces.ui.account.AccountUiState
import com.example.hundredplaces.ui.navigation.NavigationDestination
import com.example.hundredplaces.ui.theme.HundredPlacesTheme

object PlacesDestination : NavigationDestination {
    override val route = "Places"
    override val title = R.string.places
    override val iconRes = R.drawable.rounded_museum_24
}

/**
 * Entry route for Places screen
 */
@Composable
fun PlacesScreen(
    navigateToPlaceEntry: (Long) -> Unit,
    contentType: AppContentType,
    accountUiState: AccountUiState,
    viewModel: PlacesViewModel,
    placesUiState: PlacesUiState,
    modifier: Modifier = Modifier
) {
    if (contentType == AppContentType.LIST_ONLY) {
        PlacesListOnlyContent(
            uiState = placesUiState,
            cardOnClick = viewModel::selectPlaceCard,
            navigateToPlaceEntry = navigateToPlaceEntry,
            modifier = modifier
        )
    }
    else {
        PlacesListAndDetailsContent(
            accountUiState = accountUiState,
            viewModel = viewModel,
            placesUiState = placesUiState,
            cardOnClick = viewModel::selectPlaceCard,
            modifier = modifier
        )
    }
}

@Composable
private fun PlacesListOnlyContent(
    uiState: PlacesUiState,
    cardOnClick: (PlaceWithCityAndImages) -> Unit,
    navigateToPlaceEntry: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        items(uiState.places, key = { it.place.id}) {
            PlaceItem(
                placeWithCityAndImages = it,
                selected = false,
                onClick = { cardOnClick(it)
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
    cardOnClick: (PlaceWithCityAndImages) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        LazyColumn (
            modifier = Modifier
                .weight(1f)
                .padding(
                    end = 0.dp,
                    top = dimensionResource(id = R.dimen.padding_small),
                    start = dimensionResource(id = R.dimen.padding_small)
                )
        ) {
            items(
                placesUiState.places,
                key = { it.place.id }) {
                PlaceItem(
                    placeWithCityAndImages = it,
                    selected = placesUiState.currentSelectedPlace.place.id == it.place.id,
                    onClick = { cardOnClick(it) },
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

/**
 * Individual Place item
 */
@Composable
fun PlaceItem(
    selected: Boolean,
    onClick: () -> Unit,
    placeWithCityAndImages: PlaceWithCityAndImages,
    modifier: Modifier = Modifier
) {
    val color by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.tertiaryContainer
        else MaterialTheme.colorScheme.secondaryContainer,
        label = "Color Animation"
    )
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = onClick,
        modifier = modifier
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color)
        ){
            Image(
                modifier = Modifier
                    .size(64.dp)
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = when(placeWithCityAndImages.place.type) {
                    PlaceTypeEnum.MUSEUM -> R.drawable.rounded_museum_24
                    PlaceTypeEnum.PEAK -> R.drawable.rounded_landscape_24
                    PlaceTypeEnum.GALLERY -> R.drawable.rounded_wall_art_24
                    PlaceTypeEnum.CAVE -> R.drawable.icons8_cave_96
                    PlaceTypeEnum.CHURCH -> R.drawable.rounded_church_24
                    PlaceTypeEnum.SANCTUARY -> R.drawable.icons8_synagogue_96
                    PlaceTypeEnum.FORTRESS -> R.drawable.rounded_fort_24
                    PlaceTypeEnum.TOMB -> R.drawable.icons8_tomb_100
                    PlaceTypeEnum.MONUMENT -> R.drawable.icons8_obelisk_100
                    PlaceTypeEnum.WATERFALL -> R.drawable.rounded_waves_24
                    PlaceTypeEnum.OTHER -> R.drawable.icons8_other_100
                }),
                contentDescription = null
            )
            PlaceInformation(
                placeName = placeWithCityAndImages.place.name,
                placeCity = placeWithCityAndImages.city,
                modifier = Modifier.weight(1f)
            )
            PlaceRating(
                placeRating = placeWithCityAndImages.place.rating,
                modifier = Modifier
                    .padding(end = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
fun PlaceInformation(
    placeName: String,
    placeCity: String,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        Text(
            text = placeName,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = placeCity,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun PlaceRating(
    placeRating: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.round_star_rate_24),
            contentDescription = null,
            tint = Color.Yellow
        )
        Text(
            text = placeRating.toString()
        )
    }
}

/**
 * Preview for place card
 */
@Preview(showBackground = true)
@Composable
private fun PlaceCardPreview() {
    HundredPlacesTheme {
        Surface {
            PlaceItem(
                selected = true,
                placeWithCityAndImages = FakePlaceDataSource.PlacesList[0],
                onClick = {}
            )
        }
    }
}