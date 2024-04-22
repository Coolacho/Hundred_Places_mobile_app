package com.example.hundredplaces.ui.achievements

enum class AchievementTypes(
    val firstMilestone: Int,
    val secondMilestone: Int,
    val thirdMilestone: Int
) {
    MUSEUM(25, 50, 100),
    PEAK(5, 10, 20),
    GALLERY(5, 10, 15),
    CAVE(3, 5, 10),
    CHURCH(10, 30, 50),
    SANCTUARY(5, 10, 15),
    FORTRESS(1, 3, 5),
    TOMB(1, 3, 5),
    MONUMENT(10, 30, 50),
    WATERFALL(10, 20, 30),
    OTHER(5, 10, 15),
    HUNDRED_PLACES(25, 50, 100),
    TOTAL(100, 150, 200)
}