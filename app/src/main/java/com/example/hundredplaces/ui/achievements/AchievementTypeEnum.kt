package com.example.hundredplaces.ui.achievements

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.hundredplaces.R

enum class AchievementTypeEnum(
    @DrawableRes
    val icon: Int,
    @StringRes
    val title: Int,
    val firstMilestone: Int,
    val secondMilestone: Int,
    val thirdMilestone: Int
) {
    MUSEUM(R.drawable.rounded_museum_24, R.string.museums_visited, 25, 50, 100),
    PEAK(R.drawable.rounded_landscape_24, R.string.peaks_visited, 5, 10, 20),
    GALLERY(R.drawable.rounded_wall_art_24, R.string.galleries_visited, 5, 10, 15),
    CAVE(R.drawable.icons8_cave_96, R.string.caves_visited, 3, 5, 10),
    CHURCH(R.drawable.rounded_church_24, R.string.churches_visited, 10, 30, 50),
    RESERVE(R.drawable.rounded_nature_24, R.string.reserves_visited, 5, 10, 15),
    FORTRESS(R.drawable.rounded_fort_24, R.string.fortresses_visited, 1, 3, 5),
    TOMB(R.drawable.icons8_tomb_100, R.string.toms_visited, 1, 3, 5),
    MONUMENT(R.drawable.icons8_obelisk_100, R.string.monuments_visited, 10, 30, 50),
    WATERFALL(R.drawable.rounded_waves_24, R.string.waterfalls_visited, 10, 20, 30),
    OTHER(R.drawable.baseline_location_pin_24, R.string.others_visited, 5, 10, 15),
    HUNDRED_PLACES(R.drawable.rounded_money_24, R.string.hundred_places_visited, 25, 50, 100),
    TOTAL(R.drawable.icons8_check_all_96, R.string.total_visited_places, 100, 150, 200)
}