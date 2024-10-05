package com.example.hundredplaces.ui.places

import android.content.Context
import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.hundredplaces.R
import com.example.hundredplaces.data.model.place.PlaceTypeEnum
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import kotlinx.coroutines.launch
import kotlin.math.round

/**
 * Individual Place item
 */
enum class DragValue { Start, Center, End }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaceItem(
    placeWithCityAndImages: PlaceWithCityAndImages,
    isSelected: Boolean,
    distance: Float?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val anchors = with(density) {
        DraggableAnchors {
            DragValue.Start at -90.dp.toPx()
            DragValue.Center at 0f
            DragValue.End at 90.dp.toPx()
        }
    }
    val state = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Center,
            anchors = anchors,
            positionalThreshold = { totalDistance: Float -> totalDistance * 0.5f },
            velocityThreshold = { with(density) { 50.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = exponentialDecay()
        )
    }
    val color by animateColorAsState(
        targetValue = if (isSelected || state.currentValue != DragValue.Center) MaterialTheme.colorScheme.tertiaryContainer
        else MaterialTheme.colorScheme.secondaryContainer,
        label = "Color Animation"
    )
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .anchoredDraggable(
                state = state,
                orientation = Orientation.Horizontal
            )
            .clip(CardDefaults.shape)
            .background(Color.LightGray)
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .zIndex(0f)
                .width(90.dp)
        ) {
            IconButton(
                onClick = {
                    sharePlace(context, placeWithCityAndImages.place.name)
                    coroutineScope.launch {
                        state.animateTo(DragValue.Center)
                    } },
                modifier = Modifier
                    .clip(CircleShape)
                    .size(36.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.rounded_ios_share_24),
                    contentDescription = "Share button"
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier
                    .clip(CircleShape)
                    .size(36.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.rounded_favorite_24),
                    contentDescription = "Favorite button"
                )
            }
        }
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .zIndex(0f)
                .width(90.dp)
        ) {
            IconButton(
                onClick = {
                    sharePlace(context, placeWithCityAndImages.place.name)
                    coroutineScope.launch {
                        state.animateTo(DragValue.Center)
                    } },
                modifier = Modifier
                    .clip(CircleShape)
                    .size(36.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.rounded_ios_share_24),
                    contentDescription = "Share button"
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier
                    .clip(CircleShape)
                    .size(36.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.rounded_favorite_24),
                    contentDescription = "Favorite button"
                )
            }
        }
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            onClick = onClick,
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = state
                            .requireOffset()
                            .toInt(), y = 0
                    )
                }
                .align(Alignment.Center)
                .zIndex(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(color)
            ) {
                Image(
                    modifier = Modifier
                        .size(64.dp)
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(
                        id = when (placeWithCityAndImages.place.type) {
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
                        }
                    ),
                    contentDescription = null
                )
                PlaceInformation(
                    placeName = placeWithCityAndImages.place.name,
                    placeCity = placeWithCityAndImages.city,
                    modifier = Modifier.weight(1f)
                )
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.padding_small))
                ) {
                    PlaceRating(
                        placeRating = placeWithCityAndImages.place.rating
                    )
                    if (distance == null) {
                        Text(text = "-" + stringResource(R.string.metres))
                    } else {
                        if (distance > 1000) {
                            Text(
                                text = round(distance / 1000).toInt()
                                    .toString() + stringResource(R.string.kilometres)
                            )
                        } else {
                            Text(
                                text = round(distance).toInt()
                                    .toString() + stringResource(R.string.metres)
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
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
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

fun sharePlace(
    context: Context,
    placeName: String
) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Check out this place: $placeName!")
        type = "text/plain"
    }
    val intentChooser = Intent.createChooser(shareIntent, null)

    context.startActivity(intentChooser)
}