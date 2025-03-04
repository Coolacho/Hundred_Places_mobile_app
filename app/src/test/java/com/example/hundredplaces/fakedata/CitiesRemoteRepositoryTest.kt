package com.example.hundredplaces.fakedata

import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.datasources.CitiesRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class CitiesRemoteRepositoryTest {

//    private val repository = CitiesRemoteDataSource(
//        citiesRestApi = FakeCitiesRestApi()
//    )
//    @Test
//    fun citiesRemoteRepository_getAllCities_verifyCities() =
//        runTest {
//            assertEquals(repository.getAllCities(), FakeCityDataSource.citiesList)
//        }
//
//    @Test
//    fun citiesRemoteRepository_getCity_verifyCity() =
//        runTest {
//            assertEquals(repository.getCity(0), FakeCityDataSource.citiesList[0])
//        }
//
//    @Test
//    fun citiesRemoteRepository_insertCity_verifyCityInserted() =
//        runTest {
//            val city = City(
//                id = 4,
//                name = "Burgas"
//            )
//            repository.insertCity(city)
//            assertTrue(FakeCityDataSource.citiesList.contains(city))
//        }
//
//    @Test
//    fun citiesRemoteRepository_insertCity_verifyCityUnique()
//        {
//            //Test with existing id
//            assertThrows(IllegalArgumentException::class.java) {
//                runTest {
//                    val city = City(
//                        id = 3,
//                        name = "Stara Zagora"
//                    )
//                    repository.insertCity(city)
//                }
//            }
//            //Test with existing name
//            assertThrows(IllegalArgumentException::class.java) {
//                runTest {
//                    val city = City(
//                        id = 4,
//                        name = "Sofia"
//                    )
//                    repository.insertCity(city)
//                }
//            }
//        }
//
//    @Test
//    fun citiesRemoteRepository_updateCity_verifyCityUpdated() =
//        runTest {
//            val city = City(
//                id = 2,
//                name = "Stara Zagora"
//            )
//            repository.updateCity(city)
//            assertEquals(FakeCityDataSource.citiesList[1], city)
//        }
//
//    @Test
//    fun citiesRemoteRepository_deleteCity_verifyCityDeleted() =
//        runTest {
//            val city = City(
//                id = 1,
//                name = "Sofia"
//            )
//            repository.deleteCity(city)
//            assertFalse(FakeCityDataSource.citiesList.contains(city))
//        }
//
//    @Test
//    fun citiesRemoteRepository_deleteCity_verifyCityNotExists()
//        {
//            assertThrows(NoSuchElementException::class.java) {
//                runTest {
//                    val city = City(
//                        id = 4,
//                        name = "Sofia"
//                    )
//                    repository.deleteCity(city)
//                }
//            }
//        }
}