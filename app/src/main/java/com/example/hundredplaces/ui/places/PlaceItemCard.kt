package com.example.hundredplaces.ui.places

import android.content.Context
import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hundredplaces.R
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import kotlin.math.round

/**
 * Individual Place item
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaceItem(
    placeWithCityAndImages: PlaceWithCityAndImages,
    userRating: Double,
    isFavorite: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    saveRating: (placeId: Long, rating: Double, isFavorite: Boolean) -> Unit,
    toggleFavorite: (placeId: Long, rating: Double, isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val color by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.surfaceVariant
        else MaterialTheme.colorScheme.surfaceContainerHigh,
        label = "Color Animation"
    )
    val context = LocalContext.current
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(
                         if (placeWithCityAndImages.images.isNotEmpty()) placeWithCityAndImages.images[0] else ""
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                modifier = Modifier
                    .size(128.dp, 110.dp)
                    .padding(end = dimensionResource(id = R.dimen.padding_small))
                    .clip(MaterialTheme.shapes.small)
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .height(110.dp)
            ) {
                Row {
                    PlaceInformation(
                        placeName = placeWithCityAndImages.place.name,
                        placeCity = placeWithCityAndImages.city,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        PlaceRating(
                            rating = placeWithCityAndImages.place.rating
                        )
                    }
                }
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.padding_small))
                ) {

                    items(ItemActionsEnum.entries.toList()) {

                        val (label, icon, onClick) = when (it) {

                            ItemActionsEnum.RATE -> Triple(
                                R.string.rate,
                                R.drawable.round_star_rate_24
                            ) {}

                            ItemActionsEnum.FAVORITE -> Triple(
                                if (isFavorite) R.string.remove_from_favorites else R.string.add_to_favorites,
                                if (isFavorite) R.drawable.round_favorite_filled_24 else R.drawable.rounded_favorite_24
                            ) { toggleFavorite(placeWithCityAndImages.place.id, userRating, isFavorite) }

                            ItemActionsEnum.SHARE -> Triple(
                                R.string.share,
                                R.drawable.rounded_ios_share_24
                            ) { sharePlace(context, placeWithCityAndImages.place.name) }

                        }

                        Button(
                            onClick = onClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceBright,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp),
                            modifier = Modifier
                                .padding(end = dimensionResource(R.dimen.padding_small))
                        ) {
                            Icon(
                                painter = painterResource(icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = dimensionResource(R.dimen.padding_small))
                            )
                            Text(
                                text = stringResource(label),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceInformation(
    placeName: String,
    placeCity: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = placeName,
            style = MaterialTheme.typography.titleMedium.copy(
                lineBreak = LineBreak.Paragraph.copy(
                    strategy = LineBreak.Strategy.Balanced
                )
            ),
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,

        )
        Text(
            text = placeCity,
            style = MaterialTheme.typography.labelMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
        )
    }
}

@Composable
fun PlaceRating(
    rating: Double,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelMedium
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.round_star_rate_24),
            contentDescription = null,
            tint = Color.Yellow,
            modifier = Modifier
                .size(20.dp)
        )
        Text(
            text = rating.toString(),
            style = style,
            fontSize = 14.sp
        )
    }
}

@Composable
fun PlaceDistance(
    distance: Float?,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelMedium
) {
    if (distance == null) {
        Text(
            text = "-" + stringResource(R.string.metres),
            style = style,
            modifier = modifier
        )
    } else {
        if (distance > 1000) {
            Text(
                text = round(distance / 1000).toInt()
                    .toString() + stringResource(R.string.kilometres),
                style = style,
                modifier = modifier
            )
        } else {
            Text(
                text = round(distance).toInt()
                    .toString() + stringResource(R.string.metres),
                style = style,
                modifier = modifier
            )
        }
    }
}

fun sharePlace(
    context: Context,
    placeName: String
) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.place_share_text).format(placeName))
        type = "text/plain"
    }
    val intentChooser = Intent.createChooser(shareIntent, null)

    context.startActivity(intentChooser)
}