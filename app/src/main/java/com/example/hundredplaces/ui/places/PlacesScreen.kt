package com.example.hundredplaces.ui.places

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hundredplaces.R
import com.example.hundredplaces.data.FakePlaceDataSource
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.ui.AppViewModelProvider
import com.example.hundredplaces.ui.navigation.NavigationDestination
import com.example.hundredplaces.ui.theme.HundredPlacesTheme

object PlacesDestination : NavigationDestination {
    override val route = "Places"
    override val iconRes = R.drawable.rounded_museum_24
}

/**
 * Entry route for Places screen
 */
@Composable
fun PlacesScreen(
    navigateToPlaceEntry: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlacesViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    LazyColumn(
        modifier = modifier
    ) {
        items(FakePlaceDataSource.PlacesList, key = { it.place.id}) {
            PlaceItem(
                placeWithCityAndImages = it,
                //onClick = {viewModel.selectPlaceCard(it)},
                selected = uiState.value.currentSelectedPlace?.place?.id == it.place.id,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .fillMaxWidth()
                    .clickable { navigateToPlaceEntry(it.place.id) }
            )
        }
    }
}

/**
 * Individual Place item
 */
@Composable
fun PlaceItem(
    selected: Boolean,
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
                painter = painterResource(id = R.drawable.rounded_museum_24),
                contentDescription = null
            )
            PlaceInformation(
                placeName = placeWithCityAndImages.place.name,
                placeCity = placeWithCityAndImages.city
            )
            Spacer(modifier = Modifier.weight(1f))
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
                placeWithCityAndImages = FakePlaceDataSource.PlacesList[0]
            )
        }
    }
}

/**
 * Preview for place item card
 */
@Preview(showBackground = true)
@Composable
fun PlaceListPreview() {
    HundredPlacesTheme {
        Surface {
            PlacesScreen({})
        }
    }
}