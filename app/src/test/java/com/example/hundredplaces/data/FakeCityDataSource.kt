package com.example.hundredplaces.data

import com.example.hundredplaces.data.model.city.City

object FakeCityDataSource {

    //Cities' ids
    private const val ID_ONE = 1L
    private const val ID_TWO = 2L
    private const val ID_THREE = 3L

    //Cities' names
    private const val CITY_ONE = "Sofia"
    private const val CITY_TWO = "Plovdiv"
    private const val CITY_THREE = "Varna"

    //Cities list
    val citiesList = mutableListOf(
        City(
            id = ID_ONE,
            name = CITY_ONE
        ),
        City(
            id = ID_TWO,
            name = CITY_TWO
        ),
        City(
            id = ID_THREE,
            name = CITY_THREE
        )
    )
}