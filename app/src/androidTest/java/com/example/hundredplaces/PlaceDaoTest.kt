package com.example.hundredplaces

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hundredplaces.data.HundredPlacesLocalDatabase
import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.CityDao
import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceDao
import com.example.hundredplaces.data.model.place.PlaceTypeEnum
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PlaceDaoTest {
    private lateinit var cityDao: CityDao
    private lateinit var placeDao: PlaceDao
    private lateinit var hundredPlacesLocalDatabase: HundredPlacesLocalDatabase

    private var city1 = City(1, "Plovdiv")
    private var city2 = City(2, "Sofia")

    private var place1 = Place(
        1,
        "Национален исторически музей",
        42.14748477241597,
        24.751403134086157,
        4.7,
        "",
        false,
        PlaceTypeEnum.OTHER,
        1)
    private var place2 = Place(2,
        "Амфитеатър",
        42.65542986025372,
        23.271044935725428,
        4.6,
        "",
        true,
        PlaceTypeEnum.MUSEUM,
        2
        )

//    private suspend fun addOneCityToDb() {
//        cityDao.insert(city1)
//    }
//    private suspend fun addTwoCitiesToDb() {
//        cityDao.insert(city1)
//        cityDao.insert(city2)
//    }
//
//    private suspend fun addOnePlaceToDb() {
//        placeDao.insert(place1)
//    }
//
//    private suspend fun addTwoPlacesToDb() {
//        placeDao.insert(place1)
//        placeDao.insert(place2)
//    }
//
//    @Before
//    fun createDb() {
//        val context: Context = ApplicationProvider.getApplicationContext()
//        hundredPlacesLocalDatabase = Room.inMemoryDatabaseBuilder(context, HundredPlacesLocalDatabase::class.java)
//            .allowMainThreadQueries()
//            .build()
//        cityDao = hundredPlacesLocalDatabase.cityDao()
//        placeDao = hundredPlacesLocalDatabase.placeDao()
//    }
//
//    @After
//    @Throws(IOException::class)
//    fun closeDb() {
//        hundredPlacesLocalDatabase.close()
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun daoInsert_insertsPlaceIntoDb() = runBlocking {
//        addOneCityToDb()
//        addOnePlaceToDb()
//        val allPlaces = placeDao.getAllPlacesWithCityAndImages()
//        Assert.assertEquals(allPlaces[0], place1)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun daoGetAllPlaces_returnsAllPlacesFromDB() = runBlocking {
//        addTwoCitiesToDb()
//        addTwoPlacesToDb()
//        val allPlaces = placeDao.getAllPlacesWithCityAndImages()
//        Assert.assertEquals(allPlaces[0], place1)
//        Assert.assertEquals(allPlaces[1], place2)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun daoUpdatePlaces_updatesPlacesInDB() = runBlocking {
//        addTwoCitiesToDb()
//        addTwoPlacesToDb()
//        placeDao.update(place1.copy(rating = 3.0))
//        placeDao.update(place2.copy(rating = 2.0))
//
//        val allPlaces = placeDao.getAllPlacesWithCityAndImages()
//        Assert.assertEquals(allPlaces[0], place1.copy(rating = 3.0))
//        Assert.assertEquals(allPlaces[1], place2.copy(rating = 2.0))
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun daoDeletePlaces_deletesAllPlacesFromDB() = runBlocking {
//        addTwoCitiesToDb()
//        addTwoPlacesToDb()
//        placeDao.delete(place1)
//        placeDao.delete(place2)
//        val allPlaces = placeDao.getAllPlacesWithCityAndImages()
//        Assert.assertTrue(allPlaces.isEmpty())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun daoGetPlace_returnsPlaceFromDB() = runBlocking {
//        addOneCityToDb()
//        addOnePlaceToDb()
//        val place = placeDao.getPlace(1)
//        Assert.assertEquals(place, place1)
//    }
}