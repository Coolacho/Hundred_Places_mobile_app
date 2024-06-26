package com.example.hundredplaces.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.CityDao
import com.example.hundredplaces.data.model.image.Image
import com.example.hundredplaces.data.model.image.ImageDao
import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceDao
import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.UserDao
import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.VisitDao
import com.example.hundredplaces.util.LocalDateTimeConverter

@Database(
    entities = [
        City::class,
        Place::class,
        Image::class,
        User::class,
        Visit::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    value = [
        LocalDateTimeConverter::class
    ]
)
abstract class HundredPlacesLocalDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun placeDao(): PlaceDao
    abstract fun imageDao(): ImageDao
    abstract fun userDao(): UserDao
    abstract fun visitDao(): VisitDao

    companion object{
        @Volatile
        private var Instance: HundredPlacesLocalDatabase? = null

        fun getDatabase(context: Context) : HundredPlacesLocalDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, HundredPlacesLocalDatabase::class.java, "local_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}