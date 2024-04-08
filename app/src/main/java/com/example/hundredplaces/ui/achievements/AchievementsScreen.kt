package com.example.hundredplaces.ui.achievements

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.navigation.NavigationDestination

object AchievementsDestination : NavigationDestination {
    override val route = "Achievements"
    override val iconRes = R.drawable.rounded_trophy_24
}

/**
 * Entry route for Achievements screen
 */
@Composable
fun AchievementsScreen(
    modifier: Modifier = Modifier
) {
    Row {
        Text(text = "This is the achievements screen")
    }
}