package com.example.hundredplaces.data.model.city.repositories

import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.util.NetworkConnection

class CitiesDataRepository(
    private val citiesLocalRepository: CitiesLocalRepository,
    private val citiesRemoteRepository: CitiesRemoteRepository,
    private val networkConnection: NetworkConnection
) : CitiesRepository{

    override suspend fun getAllCities(): List<City> {
        return if (networkConnection.isNetworkConnected) {
            citiesRemoteRepository.getAllCities()
        } else {
            citiesLocalRepository.getAllCities()
        }
    }

    override suspend fun getCity(id: Long): City {
        return if (networkConnection.isNetworkConnected) {
            citiesRemoteRepository.getCity(id)
        } else {
            citiesLocalRepository.getCity(id)
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