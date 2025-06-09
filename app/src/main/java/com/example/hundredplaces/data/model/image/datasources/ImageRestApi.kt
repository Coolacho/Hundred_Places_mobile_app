package com.example.hundredplaces.data.model.image.datasources

import com.example.hundredplaces.data.model.image.Image
import retrofit2.http.GET

interface ImageRestApi {
    @GET("/api/v1/images")
    suspend fun getAllImages(): List<Image>

}