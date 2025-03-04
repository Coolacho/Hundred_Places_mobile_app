package com.example.hundredplaces.fakedata

import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.CitiesRestApi

class FakeCitiesRestApi {
//    override suspend fun getAllCities(): List<City> {
//        return FakeCityDataSource.citiesList
//    }
//
//    override suspend fun getCity(id: Long): City {
//        return FakeCityDataSource.citiesList[0]
//    }
//
//    override suspend fun insertCity(city: City) {
//        FakeCityDataSource.citiesList.forEach {
//            if ((it.id == city.id) or (it.name == city.name)) {
//                throw IllegalArgumentException("City's id and name should be unique!")
//            }
//        }
//        FakeCityDataSource.citiesList.add(city)
//    }
//
//    override suspend fun updateCity(city: City) {
//        FakeCityDataSource.citiesList.forEachIndexed { index, oldCity ->
//            if (oldCity.id == city.id) {
//                FakeCityDataSource.citiesList[index] = city
//            }
//        }
//    }
//
//    override suspend fun deleteCity(city: City) {
//        if (!FakeCityDataSource.citiesList.remove(city)) {
//            throw NoSuchElementException("This element doesn't exist!")
//        }
//    }
}