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
import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.UserDao
import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.VisitDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class VisitDaoTest {
    private lateinit var visitDao: VisitDao
    private lateinit var cityDao: CityDao
    private lateinit var placeDao: PlaceDao
    private lateinit var userDao: UserDao
    private lateinit var hundredPlacesLocalDatabase: HundredPlacesLocalDatabase

    private val visit1 = Visit(id= UUID.fromString("11efea33-dbf6-d03f-b9ef-2cf05d72c5a3"), userId=1, placeId=1, dateVisited= LocalDateTime.parse("2024-01-03T10:15:30"))
    private val visit2 = Visit(userId=1, placeId=1, dateVisited= LocalDateTime.parse("2024-01-03T10:16:30"))

    private val city1 = City(1, "Plovdiv")

    private val place1 = Place(
        1,
        "Национален исторически музей",
        42.14748477241597,
        24.751403134086157,
        4.7,
        "",
        false,
        PlaceTypeEnum.OTHER,
        1)

    private val user1 = User(
        name = "Acho",
        email = "acho@acho.com",
        password = "aaaachoooo"
    )

    private suspend fun addVisitToDb(visit: Visit) : Long {
        return visitDao.insert(visit)
    }

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        hundredPlacesLocalDatabase = Room.inMemoryDatabaseBuilder(context, HundredPlacesLocalDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        visitDao = hundredPlacesLocalDatabase.visitDao()
        cityDao = hundredPlacesLocalDatabase.cityDao()
        placeDao = hundredPlacesLocalDatabase.placeDao()
        userDao = hundredPlacesLocalDatabase.userDao()
    }

    @Before
    fun populateDb() {
        runBlocking {
            cityDao.insertAll(listOf(city1))
            placeDao.insertAll(listOf(place1))
            userDao.insert(user1)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        hundredPlacesLocalDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsVisitIntoDb() = runBlocking {
        addVisitToDb(visit1)
        val allVisits = visitDao.getAllVisitsByUserId(1).first()
        println(allVisits)
        assertEquals(allVisits[0], visit1)
    }

    @Test
    fun daoInsert_insertVisitIntoDb_failSameDateCheck() = runBlocking {
        val res1 = addVisitToDb(visit1)
        val res2 = addVisitToDb(visit2)
        val allVisits = visitDao.getAllVisitsByUserId(1).first()
        println(res1)
        println(res2)
        println(allVisits)
        assertEquals(allVisits.size, 1)
    }
}