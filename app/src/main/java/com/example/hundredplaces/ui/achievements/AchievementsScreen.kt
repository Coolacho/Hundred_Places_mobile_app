package com.example.hundredplaces.ui.achievements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.account.AccountUiState
import com.example.hundredplaces.ui.navigation.NavigationDestination

object AchievementsDestination : NavigationDestination {
    override val route = "Achievements"
    override val title = R.string.achievements
    override val iconRes = R.drawable.rounded_trophy_24
}

/**
 * Entry route for Achievements screen
 */
@Composable
fun AchievementsScreen(
    uiState: AccountUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        items(AchievementTypes.entries.toTypedArray()) {
            Card(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        modifier = Modifier
                            .size(64.dp)
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .clip(MaterialTheme.shapes.small),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = when(it) {
                            AchievementTypes.MUSEUM -> R.drawable.rounded_museum_24
                            AchievementTypes.PEAK -> R.drawable.rounded_landscape_24
                            AchievementTypes.GALLERY -> R.drawable.rounded_wall_art_24
                            AchievementTypes.CAVE -> R.drawable.icons8_cave_96
                            AchievementTypes.CHURCH -> R.drawable.rounded_church_24
                            AchievementTypes.SANCTUARY -> R.drawable.icons8_synagogue_96
                            AchievementTypes.FORTRESS -> R.drawable.rounded_fort_24
                            AchievementTypes.TOMB -> R.drawable.icons8_tomb_100
                            AchievementTypes.MONUMENT -> R.drawable.icons8_obelisk_100
                            AchievementTypes.WATERFALL -> R.drawable.rounded_waves_24
                            AchievementTypes.OTHER -> R.drawable.icons8_other_100
                            AchievementTypes.HUNDRED_PLACES -> R.drawable.rounded_money_24
                            AchievementTypes.TOTAL -> R.drawable.icons8_check_all_96
                        }),
                        contentDescription = null
                    )
                    Column(
                        modifier = Modifier
                            .padding(end = dimensionResource(id = R.dimen.padding_medium))
                            .fillMaxWidth()
                    ) {
                        Text(text = when(it) {
                            AchievementTypes.MUSEUM -> stringResource(R.string.museums_visited)
                            AchievementTypes.PEAK -> stringResource(R.string.peaks_visited)
                            AchievementTypes.GALLERY -> stringResource(R.string.galleries_visited)
                            AchievementTypes.CAVE -> stringResource(R.string.caves_visited)
                            AchievementTypes.CHURCH -> stringResource(R.string.churches_visited)
                            AchievementTypes.SANCTUARY -> stringResource(R.string.sanctuaries_visited)
                            AchievementTypes.FORTRESS -> stringResource(R.string.fortresses_visited)
                            AchievementTypes.TOMB -> stringResource(R.string.toms_visited)
                            AchievementTypes.MONUMENT -> stringResource(R.string.monuments_visited)
                            AchievementTypes.WATERFALL -> stringResource(R.string.waterfalls_visited)
                            AchievementTypes.OTHER -> stringResource(R.string.others_visited)
                            AchievementTypes.HUNDRED_PLACES -> stringResource(R.string.hundred_places_visited)
                            AchievementTypes.TOTAL -> stringResource(R.string.total_visited_places)
                        })
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = when(it) {
                                AchievementTypes.MUSEUM -> uiState.currentUser.museumsVisited.toString()
                                AchievementTypes.PEAK -> uiState.currentUser.peaksVisited.toString()
                                AchievementTypes.GALLERY -> uiState.currentUser.galleriesVisited.toString()
                                AchievementTypes.CAVE -> uiState.currentUser.cavesVisited.toString()
                                AchievementTypes.CHURCH -> uiState.currentUser.churchesVisited.toString()
                                AchievementTypes.SANCTUARY -> uiState.currentUser.sanctuariesVisited.toString()
                                AchievementTypes.FORTRESS -> uiState.currentUser.fortressesVisited.toString()
                                AchievementTypes.TOMB -> uiState.currentUser.tombsVisited.toString()
                                AchievementTypes.MONUMENT -> uiState.currentUser.monumentsVisited.toString()
                                AchievementTypes.WATERFALL -> uiState.currentUser.waterfallsVisited.toString()
                                AchievementTypes.OTHER -> uiState.currentUser.othersVisited.toString()
                                AchievementTypes.HUNDRED_PLACES -> uiState.currentUser.hundredPlacesVisited.toString()
                                AchievementTypes.TOTAL -> uiState.currentUser.totalVisited.toString()
                            })
                            LinearProgressIndicator(
                                progress = { when(it) {
                                    AchievementTypes.MUSEUM -> uiState.currentUser.museumsVisited/getUpperBound(AchievementTypes.MUSEUM, uiState.currentUser.museumsVisited).toFloat()
                                    AchievementTypes.PEAK -> uiState.currentUser.peaksVisited/getUpperBound(AchievementTypes.PEAK, uiState.currentUser.peaksVisited).toFloat()
                                    AchievementTypes.GALLERY -> uiState.currentUser.galleriesVisited/getUpperBound(AchievementTypes.GALLERY, uiState.currentUser.galleriesVisited).toFloat()
                                    AchievementTypes.CAVE -> uiState.currentUser.cavesVisited/getUpperBound(AchievementTypes.CAVE, uiState.currentUser.cavesVisited).toFloat()
                                    AchievementTypes.CHURCH -> uiState.currentUser.churchesVisited/getUpperBound(AchievementTypes.CHURCH, uiState.currentUser.churchesVisited).toFloat()
                                    AchievementTypes.SANCTUARY -> uiState.currentUser.sanctuariesVisited/getUpperBound(AchievementTypes.SANCTUARY, uiState.currentUser.sanctuariesVisited).toFloat()
                                    AchievementTypes.FORTRESS -> uiState.currentUser.fortressesVisited/getUpperBound(AchievementTypes.FORTRESS, uiState.currentUser.fortressesVisited).toFloat()
                                    AchievementTypes.TOMB -> uiState.currentUser.tombsVisited/getUpperBound(AchievementTypes.TOMB, uiState.currentUser.tombsVisited).toFloat()
                                    AchievementTypes.MONUMENT -> uiState.currentUser.monumentsVisited/getUpperBound(AchievementTypes.MONUMENT, uiState.currentUser.monumentsVisited).toFloat()
                                    AchievementTypes.WATERFALL -> uiState.currentUser.waterfallsVisited/getUpperBound(AchievementTypes.WATERFALL, uiState.currentUser.waterfallsVisited).toFloat()
                                    AchievementTypes.OTHER -> uiState.currentUser.othersVisited/getUpperBound(AchievementTypes.OTHER, uiState.currentUser.othersVisited).toFloat()
                                    AchievementTypes.HUNDRED_PLACES -> uiState.currentUser.hundredPlacesVisited/getUpperBound(AchievementTypes.HUNDRED_PLACES, uiState.currentUser.hundredPlacesVisited).toFloat()
                                    AchievementTypes.TOTAL -> uiState.currentUser.totalVisited/getUpperBound(AchievementTypes.TOTAL, uiState.currentUser.totalVisited).toFloat()
                                }},
                                strokeCap = StrokeCap.Round,
                                trackColor = Color.LightGray,
                                modifier = Modifier
                                    .padding(horizontal = dimensionResource(id = R.dimen.padding_small))
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                            Text(text = when(it) {
                                AchievementTypes.MUSEUM -> getUpperBound(AchievementTypes.MUSEUM, uiState.currentUser.museumsVisited).toString()
                                AchievementTypes.PEAK -> getUpperBound(AchievementTypes.PEAK, uiState.currentUser.peaksVisited).toString()
                                AchievementTypes.GALLERY -> getUpperBound(AchievementTypes.GALLERY, uiState.currentUser.galleriesVisited).toString()
                                AchievementTypes.CAVE -> getUpperBound(AchievementTypes.CAVE, uiState.currentUser.cavesVisited).toString()
                                AchievementTypes.CHURCH -> getUpperBound(AchievementTypes.CHURCH, uiState.currentUser.churchesVisited).toString()
                                AchievementTypes.SANCTUARY -> getUpperBound(AchievementTypes.SANCTUARY, uiState.currentUser.sanctuariesVisited).toString()
                                AchievementTypes.FORTRESS -> getUpperBound(AchievementTypes.FORTRESS, uiState.currentUser.fortressesVisited).toString()
                                AchievementTypes.TOMB -> getUpperBound(AchievementTypes.TOMB, uiState.currentUser.tombsVisited).toString()
                                AchievementTypes.MONUMENT -> getUpperBound(AchievementTypes.MONUMENT, uiState.currentUser.monumentsVisited).toString()
                                AchievementTypes.WATERFALL -> getUpperBound(AchievementTypes.WATERFALL, uiState.currentUser.waterfallsVisited).toString()
                                AchievementTypes.OTHER -> getUpperBound(AchievementTypes.OTHER, uiState.currentUser.othersVisited).toString()
                                AchievementTypes.HUNDRED_PLACES -> getUpperBound(AchievementTypes.HUNDRED_PLACES, uiState.currentUser.hundredPlacesVisited).toString()
                                AchievementTypes.TOTAL -> getUpperBound(AchievementTypes.TOTAL, uiState.currentUser.totalVisited).toString()
                            })
                        }
                    }
                }
            }
        }
    }
}

private fun getUpperBound(achievementType: AchievementTypes, visited: Int): Int {
    return if (achievementType.firstMilestone > visited) achievementType.firstMilestone
    else if (achievementType.secondMilestone > visited) achievementType.secondMilestone
    else achievementType.thirdMilestone
}