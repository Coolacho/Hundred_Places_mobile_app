package com.example.hundredplaces.ui.places

import android.content.Context
import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hundredplaces.R
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.ui.theme.Typography
import kotlin.math.round

/**
 * Individual Place item
 */

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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
    var showRatingSheet by remember { mutableStateOf(false) }
    var currentRating by remember { mutableDoubleStateOf(userRating) }
    val color by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.surfaceVariant
        else MaterialTheme.colorScheme.surfaceContainer,
        label = "Color Animation"
    )
    val context = LocalContext.current

    if (showRatingSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showRatingSheet = false
                currentRating = userRating
            },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.rate_place_name, placeWithCityAndImages.place.name), style = Typography.titleLarge)

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (num in 1..5) {
                        Box(
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    detectTapGestures { offset ->
                                        val half = offset.x < size.width / 2
                                        val newRating = if (half) (num.toDouble() - 0.5f) else num.toDouble()
                                        currentRating = newRating
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            val (starIcon, tint) = when {
                                currentRating >= num -> Pair(R.drawable.round_star_rate_24, Color(0xFFD4AF37))
                                currentRating >= num - 0.5f -> Pair(R.drawable.rounded_star_half_24, Color(0xFFD4AF37))
                                else -> Pair(R.drawable.rounded_star_24, Color.Gray)
                            }
                            Icon(
                                painter = painterResource(starIcon),
                                contentDescription = "Rate $num",
                                tint = tint,
                                modifier = Modifier
                                    .size(48.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            showRatingSheet = false
                            currentRating = userRating
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                    ),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.rounded_cancel_24),
                            contentDescription = stringResource(R.string.cancel)
                        )
                    }
                    IconButton(
                        onClick = {
                            showRatingSheet = false
                            saveRating(placeWithCityAndImages.place.id, currentRating, isFavorite)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.rounded_check_circle_24),
                            contentDescription = stringResource(R.string.save)
                        )
                    }
                }
            }
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
        modifier = modifier
            .height(140.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
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
                    .width(150.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        top = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_small),
                        start = dimensionResource(R.dimen.padding_small))
            ) {
                Row(
                    modifier = Modifier
                        .padding(end = dimensionResource(R.dimen.padding_small))
                ) {
                    PlaceInformation(
                        placeName = placeWithCityAndImages.place.name,
                        placeCity = placeWithCityAndImages.city,
                        modifier = Modifier
                            .weight(1f)
                    )
                    PlaceRating(
                        rating = placeWithCityAndImages.place.rating
                    )
                }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items(ItemActionsEnum.entries.toList()) {

                        val (label, icon, onClick) = when (it) {

                            ItemActionsEnum.RATE -> Triple(
                                R.string.rate,
                                R.drawable.round_star_rate_24
                            ) { showRatingSheet = true}

                            ItemActionsEnum.FAVORITE -> Triple(
                                if (isFavorite) R.string.remove_from_favorites else R.string.add_to_favorites,
                                if (isFavorite) R.drawable.round_favorite_filled_24 else R.drawable.rounded_favorite_24
                            ) { toggleFavorite(placeWithCityAndImages.place.id, userRating, isFavorite) }

                            ItemActionsEnum.SHARE -> Triple(
                                R.string.share,
                                R.drawable.rounded_ios_share_24
                            ) { sharePlace(context, placeWithCityAndImages.place.name, placeWithCityAndImages.place.id) }

                        }

                        Button(
                            onClick = onClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp),
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
            style = MaterialTheme.typography.titleLarge.copy(
                lineBreak = LineBreak.Paragraph.copy(
                    strategy = LineBreak.Strategy.Balanced
                ),
                lineHeight = 24.sp
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
        )
        Text(
            text = placeCity,
            style = MaterialTheme.typography.labelLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
        )
    }
}

@Composable
fun PlaceRating(
    rating: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.round_star_rate_24),
            contentDescription = null,
            tint = Color(0xFFD4AF37),
            modifier = Modifier
                .size(20.dp)
        )
        Text(
            text = rating.toString(),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    return Brush.linearGradient(
        colors = listOf(
            Color.Gray.copy(alpha = 0.3f),
            Color.Gray.copy(alpha = 0.1f),
            Color.Gray.copy(alpha = 0.3f)
        ),
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaceItemLoading(
    shimmer: Brush,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .height(140.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(shimmer)
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.padding_small))
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = dimensionResource(R.dimen.padding_small))
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(MaterialTheme.shapes.small)
                        .background(shimmerBrush())
                ) {}
                Row(
                    modifier = Modifier
                        .height(ButtonDefaults.MinHeight + dimensionResource(R.dimen.padding_small))
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .background(shimmerBrush())
                ) {}
            }
        }
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
    placeName: String,
    placeId: Long
) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.place_share_text).format(placeName) + "http://10.0.2.2:5173/places/$placeId")
        type = "text/plain"
    }
    val intentChooser = Intent.createChooser(shareIntent, null)

    context.startActivity(intentChooser)
}