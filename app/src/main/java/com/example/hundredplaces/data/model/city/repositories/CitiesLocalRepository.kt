package com.example.hundredplaces.data.model.city.repositories

import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.CityDao

class CitiesLocalRepository(
    private val cityDao: CityDao
) : CitiesRepository {

    override suspend fun getAllCities(): List<City> = cityDao.getAllCities()

    override suspend fun getCity(id: Long): City = cityDao.getCity(id)

    override suspend fun insertCity(city: City) = cityDao.insert(city)

    override suspend fun deleteCity(city: City) = cityDao.delete(city)

    override suspend fun updateCity(city: City) = cityDao.update(city)
}