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
import com.example.hundredplaces.ui.navigation.MenuNavigationDestination

object AchievementsDestination : MenuNavigationDestination {
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
        items(AchievementTypeEnum.entries.toTypedArray()) {
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
                        painter = painterResource(id = it.icon),
                        contentDescription = null
                    )
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .padding(end = dimensionResource(id = R.dimen.padding_medium))
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(it.title))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            when(it) {
                                AchievementTypeEnum.MUSEUM -> uiState.currentUser.museumsVisited to getUpperBound(it, uiState.currentUser.museumsVisited)
                                AchievementTypeEnum.PEAK -> uiState.currentUser.peaksVisited to getUpperBound(it, uiState.currentUser.peaksVisited)
                                AchievementTypeEnum.GALLERY -> uiState.currentUser.galleriesVisited to getUpperBound(it, uiState.currentUser.galleriesVisited)
                                AchievementTypeEnum.CAVE -> uiState.currentUser.cavesVisited to getUpperBound(it, uiState.currentUser.cavesVisited)
                                AchievementTypeEnum.CHURCH -> uiState.currentUser.churchesVisited to getUpperBound(it, uiState.currentUser.churchesVisited)
                                AchievementTypeEnum.SANCTUARY -> uiState.currentUser.sanctuariesVisited to getUpperBound(it, uiState.currentUser.sanctuariesVisited)
                                AchievementTypeEnum.FORTRESS -> uiState.currentUser.fortressesVisited to getUpperBound(it, uiState.currentUser.fortressesVisited)
                                AchievementTypeEnum.TOMB -> uiState.currentUser.tombsVisited to getUpperBound(it, uiState.currentUser.tombsVisited)
                                AchievementTypeEnum.MONUMENT -> uiState.currentUser.monumentsVisited to getUpperBound(it, uiState.currentUser.monumentsVisited)
                                AchievementTypeEnum.WATERFALL -> uiState.currentUser.waterfallsVisited to getUpperBound(it, uiState.currentUser.waterfallsVisited)
                                AchievementTypeEnum.OTHER -> uiState.currentUser.othersVisited to getUpperBound(it, uiState.currentUser.othersVisited)
                                AchievementTypeEnum.HUNDRED_PLACES -> uiState.currentUser.hundredPlacesVisited to getUpperBound(it, uiState.currentUser.hundredPlacesVisited)
                                AchievementTypeEnum.TOTAL -> uiState.currentUser.totalVisited to getUpperBound(it, uiState.currentUser.totalVisited)
                            }.let { (visited, upperBound) ->
                                LinearProgressIndicator(
                                    progress = { (visited.toFloat() / upperBound.toFloat()) },
                                    trackColor = Color.LightGray,
                                    strokeCap = StrokeCap.Round,
                                    gapSize = 0.dp,
                                    modifier = Modifier
                                        .padding(end = dimensionResource(R.dimen.padding_small))
                                        .weight(1f)
                                )
                                Text(
                                    text = "$visited/$upperBound".padStart(4)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getUpperBound(achievementType: AchievementTypeEnum, visited: Int): Int {
    return if (achievementType.firstMilestone > visited) achievementType.firstMilestone
    else if (achievementType.secondMilestone > visited) achievementType.secondMilestone
    else achievementType.thirdMilestone
}