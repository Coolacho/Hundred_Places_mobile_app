package com.example.hundredplaces.fakedata

import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.CityRestApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCityRestApiService : CityRestApiService {
    override suspend fun getAllCities(): Flow<List<City>> {
        return flowOf(FakeCityDataSource.citiesList)
    }

    override suspend fun getCity(id: Int): Flow<City> {
        return flowOf(FakeCityDataSource.citiesList[0])
    }

    override suspend fun insertCity(city: City) {
        FakeCityDataSource.citiesList.forEach { it ->
            if ((it.id == city.id) or (it.name == city.name)) {
                throw IllegalArgumentException("City's id and name should be unique!")
            }
        }
        FakeCityDataSource.citiesList.add(city)
    }

    override suspend fun updateCity(city: City) {
        FakeCityDataSource.citiesList.forEachIndexed { index, oldCity ->
            if (oldCity.id == city.id) {
                FakeCityDataSource.citiesList[index] = city
            }
        }
    }

    override suspend fun deleteCity(city: City) {
        if (!FakeCityDataSource.citiesList.remove(city)) {
            throw NoSuchElementException("This element doesn't exist!")
        }
    }
}