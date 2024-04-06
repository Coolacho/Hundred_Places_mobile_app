package com.example.hundredplaces.data.model.city.repositories

import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.CityRestApiService
import kotlinx.coroutines.flow.Flow

class CitiesRemoteRepository(
    private val cityRestApiService: CityRestApiService
) : CitiesRepository {

    override suspend fun getAllCitiesStream(): Flow<List<City>> = cityRestApiService.getAllCities()

    override suspend fun getCityStream(id: Int): Flow<City?> = cityRestApiService.getCity(id)

    override suspend fun insertCity(city: City) = cityRestApiService.insertCity(city)

    override suspend fun deleteCity(city: City) = cityRestApiService.deleteCity(city)

    override suspend fun updateCity(city: City) = cityRestApiService.updateCity(city)
}