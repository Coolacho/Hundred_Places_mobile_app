package com.example.hundredplaces.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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

@Database(
    entities = [
        City::class,
        Place::class,
        Image::class,
        User::class,
        Visit::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun placeDao(): PlaceDao
    abstract fun imageDao(): ImageDao
    abstract fun userDao(): UserDao
    abstract fun visitDao(): VisitDao

    companion object{
        @Volatile
        private var Instance: LocalDatabase? = null

        fun getDatabase(context: Context) : LocalDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LocalDatabase::class.java, "local_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}