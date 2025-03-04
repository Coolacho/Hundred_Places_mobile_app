package com.example.hundredplaces.ui.achievements

data class AchievementsUiState (
    val achievements: Map<AchievementTypeEnum, Int> = emptyMap()
)