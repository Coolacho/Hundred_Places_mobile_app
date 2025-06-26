package com.example.hundredplaces.ui.places

import androidx.annotation.StringRes
import com.example.hundredplaces.R

enum class PlaceFiltersEnum(
    @StringRes
    val categoryName: Int
) {
    FAVORITES(R.string.favorites_category_name),
    HUNDRED_PLACES(R.string.hundred_places_category_name),
    RATING(R.string.rating_category_name),
    MUSEUM(R.string.museums_category_name),
    PEAK(R.string.peaks_category_name),
    GALLERY(R.string.galleries_category_name),
    CAVE(R.string.caves_category_name),
    CHURCH(R.string.churches_category_name),
    RESERVE(R.string.reserves_category_name),
    FORTRESS(R.string.fortresses_category_name),
    TOMB(R.string.tombs_category_name),
    MONUMENT(R.string.monuments_category_name),
    WATERFALL(R.string.waterfalls_category_name),
    OTHER(R.string.others_category_name)
}