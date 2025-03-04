package com.example.hundredplaces.data.model.city.datasources

import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.CityDao

class CitiesLocalDataSource(
    private val cityDao: CityDao
) {
    val allCities = cityDao.getAll()

    suspend fun insertAll(cities: List<City>) = cityDao.insertAll(cities)

    suspend fun deleteCitiesNotIn(ids: List<Long>) = cityDao.deleteCitiesNotIn(ids)
}