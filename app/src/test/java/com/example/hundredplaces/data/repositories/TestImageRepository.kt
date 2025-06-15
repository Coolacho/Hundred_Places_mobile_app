package com.example.hundredplaces.data.repositories

import com.example.hundredplaces.data.model.image.repositories.ImageRepository
import kotlinx.coroutines.flow.Flow

class TestImageRepository: ImageRepository {
    override val oneImagePerPlace: Flow<List<String>>
        get() = TODO("Not yet implemented")

    override suspend fun pullImages() {
        TODO("Not yet implemented")
    }
}