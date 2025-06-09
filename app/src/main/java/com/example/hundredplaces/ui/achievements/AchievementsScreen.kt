package com.example.hundredplaces.ui.achievements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.AppViewModelProvider
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
    modifier: Modifier = Modifier,
    achievementsViewModel: AchievementsViewModel = viewModel (
        factory = AppViewModelProvider.Factory),
) {
    val uiState = achievementsViewModel.uiState.collectAsStateWithLifecycle().value

    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier
            .fillMaxSize()
    ) {
        items(uiState.achievements.entries.toList()) {
            val (upperBound, tint) = getUpperBound(it.key, it.value)
            Card {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .size(64.dp)
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .clip(MaterialTheme.shapes.small),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = it.key.icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(tint)
                    )
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .padding(end = dimensionResource(id = R.dimen.padding_medium))
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(it.key.title))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LinearProgressIndicator(
                                progress = { (it.value.toFloat() / upperBound.toFloat()) },
                                trackColor = Color.LightGray,
                                strokeCap = StrokeCap.Round,
                                gapSize = 0.dp,
                                drawStopIndicator = {}, //Removes the dots at the start and the end of the track
                                modifier = Modifier
                                    .padding(end = dimensionResource(R.dimen.padding_small))
                                    .weight(1f)
                            )
                            Text(
                                text = "${it.value}/$upperBound".padStart(4)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getUpperBound(achievementType: AchievementTypeEnum, visited: Int): Pair<Int, Color> {
    return if (achievementType.firstMilestone > visited) achievementType.firstMilestone to Color(0xFFA9671D)
    else if (achievementType.secondMilestone > visited) achievementType.secondMilestone to Color(0xFFA7A7A7)
    else achievementType.thirdMilestone to Color(0xFFD4AF37)
}