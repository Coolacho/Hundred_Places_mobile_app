package com.example.hundredplaces.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.datasources.CityDao
import com.example.hundredplaces.data.model.image.Image
import com.example.hundredplaces.data.model.image.datasources.ImageDao
import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.datasources.PlaceDao
import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferences
import com.example.hundredplaces.data.model.usersPlacesPreferences.datasources.UsersPlacesPreferencesDao
import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.datasources.UserDao
import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.datasources.VisitDao
import com.example.hundredplaces.util.InstantConverter

@Database(
    entities = [
        City::class,
        Place::class,
        Image::class,
        User::class,
        Visit::class,
        UsersPlacesPreferences::class
    ],
    version = 17,
    exportSchema = false
)
@TypeConverters(
    value = [
        InstantConverter::class
    ]
)
abstract class HundredPlacesLocalDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun placeDao(): PlaceDao
    abstract fun imageDao(): ImageDao
    abstract fun userDao(): UserDao
    abstract fun visitDao(): VisitDao
    abstract fun usersPlacesPreferencesDao(): UsersPlacesPreferencesDao

    companion object{
        @Volatile
        private var Instance: HundredPlacesLocalDatabase? = null

        fun getDatabase(context: Context) : HundredPlacesLocalDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, HundredPlacesLocalDatabase::class.java, "local_database")
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}