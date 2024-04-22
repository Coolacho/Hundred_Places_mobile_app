package com.example.hundredplaces

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hundredplaces.data.HundredPlacesLocalDatabase
import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.CityDao
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CityDaoTest {
    private lateinit var cityDao: CityDao
    private lateinit var hundredPlacesLocalDatabase: HundredPlacesLocalDatabase

    private var city1 = City(1, "Plovdiv")
    private var city2 = City(2, "Sofia")

    private suspend fun addOneCityToDb() {
        cityDao.insert(city1)
    }

    private suspend fun addTwoCitiesToDb() {
        cityDao.insert(city1)
        cityDao.insert(city2)
    }

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        hundredPlacesLocalDatabase = Room.inMemoryDatabaseBuilder(context, HundredPlacesLocalDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        cityDao = hundredPlacesLocalDatabase.cityDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        hundredPlacesLocalDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsCityIntoDb() = runBlocking {
        addOneCityToDb()
        val allCities = cityDao.getAllCities()
        assertEquals(allCities[0], city1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllCities_returnsAllCitiesFromDB() = runBlocking {
        addTwoCitiesToDb()
        val allCities = cityDao.getAllCities()
        assertEquals(allCities[0], city1)
        assertEquals(allCities[1], city2)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateCities_updatesCitiesInDB() = runBlocking {
        addTwoCitiesToDb()
        cityDao.update(City(1, "Burgas"))
        cityDao.update(City(2, "Stara Zagora"))

        val allCities = cityDao.getAllCities()
        assertEquals(allCities[0], City(1, "Burgas"))
        assertEquals(allCities[1], City(2, "Stara Zagora"))
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteCities_deletesAllCitiesFromDB() = runBlocking {
        addTwoCitiesToDb()
        cityDao.delete(city1)
        cityDao.delete(city2)
        val allCities = cityDao.getAllCities()
        Assert.assertTrue(allCities.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetCity_returnsCityFromDB() = runBlocking {
        addOneCityToDb()
        val city = cityDao.getCity(1)
        assertEquals(city, city1)
    }
}