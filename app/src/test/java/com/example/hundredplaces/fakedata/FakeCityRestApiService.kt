package com.example.hundredplaces.fakedata

import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.CityRestApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCityRestApiService : CityRestApiService {
    override suspend fun getAllCities(): Flow<List<City>> {
        return flowOf(FakeDataSource.citiesList)
    }

    override suspend fun getCity(id: Int): Flow<City> {
        return flowOf(FakeDataSource.citiesList[0])
    }

    override suspend fun insertCity(city: City) {
        FakeDataSource.citiesList.forEach { it ->
            if ((it.id == city.id) or (it.name == city.name)) {
                throw IllegalArgumentException("City's id and name should be unique!")
            }
        }
        FakeDataSource.citiesList.add(city)
    }

    override suspend fun updateCity(city: City) {
        FakeDataSource.citiesList.forEachIndexed { index, oldCity ->
            if (oldCity.id == city.id) {
                FakeDataSource.citiesList[index] = city
            }
        }
    }

    override suspend fun deleteCity(city: City) {
        if (!FakeDataSource.citiesList.remove(city)) {
            throw NoSuchElementException("This element doesn't exist!")
        }
    }
}