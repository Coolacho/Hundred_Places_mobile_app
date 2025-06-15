package com.example.hundredplaces

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hundredplaces.data.HundredPlacesLocalDatabase
import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.datasources.CityDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CityDaoTest {
    private lateinit var cityDao: CityDao
    private lateinit var hundredPlacesLocalDatabase: HundredPlacesLocalDatabase

    private var city1 = City(1, "Sofia")
    private var city2 = City(2, "Plovdiv")

    private val citiesList = listOf(city1, city2)

    private suspend fun insertCitiesToDb() {
        cityDao.insertAll(citiesList)
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
    fun daoInsert_insertsCitiesIntoDb() = runBlocking {
        insertCitiesToDb()
        val allCities = cityDao.getAll().first()
        assertEquals(allCities, citiesList)
    }

    @Test
    @Throws(Exception::class)
    fun daoDelete_deleteCitiesWhereIdsNotInFromDb() = runBlocking {
        insertCitiesToDb()
        cityDao.deleteCitiesNotIn(listOf(1))
        val allCities = cityDao.getAll().first()
        assertEquals(allCities.size, 1)
        assertEquals(allCities[0], citiesList[0])
    }

}