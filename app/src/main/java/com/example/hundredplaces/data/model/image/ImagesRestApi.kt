package com.example.hundredplaces.data.model.image

import retrofit2.http.GET

interface ImagesRestApi {
    @GET("/api/v1/images")
    suspend fun getAllImages(): List<Image>

}