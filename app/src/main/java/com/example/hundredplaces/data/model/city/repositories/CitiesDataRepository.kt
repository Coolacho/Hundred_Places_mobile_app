package com.example.hundredplaces.data.model.city.repositories

import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.flow.Flow

class CitiesDataRepository(
    private val citiesLocalRepository: CitiesLocalRepository,
    private val citiesRemoteRepository: CitiesRemoteRepository,
    private val networkConnection: NetworkConnection
) : CitiesRepository{

    override suspend fun getAllCitiesStream(): Flow<List<City>> {
        return if (networkConnection.isNetworkConnected) {
            citiesRemoteRepository.getAllCitiesStream()
        } else {
            citiesLocalRepository.getAllCitiesStream()
        }
    }

    override suspend fun getCityStream(id: Int): Flow<City?> {
        return if (networkConnection.isNetworkConnected) {
            citiesRemoteRepository.getCityStream(id)
        } else {
            citiesLocalRepository.getCityStream(id)
        }
    }

    override suspend fun insertCity(city: City) {
        return if (networkConnection.isNetworkConnected) {
            citiesRemoteRepository.insertCity(city)
        } else {
            citiesLocalRepository.insertCity(city)
        }
    }

    override suspend fun deleteCity(city: City) {
        return if (networkConnection.isNetworkConnected) {
            citiesRemoteRepository.deleteCity(city)
        } else {
            citiesLocalRepository.deleteCity(city)
        }
    }

    override suspend fun updateCity(city: City) {
        return if (networkConnection.isNetworkConnected) {
            citiesRemoteRepository.updateCity(city)
        } else {
            citiesLocalRepository.updateCity(city)
        }
    }
}