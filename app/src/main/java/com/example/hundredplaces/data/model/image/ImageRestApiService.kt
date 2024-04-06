package com.example.hundredplaces.data.model.image

import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ImageRestApiService {
    @GET("images")
    suspend fun getAllImages(): Flow<List<Image>>

    @GET("images/{imageId}")
    suspend fun getImage(@Path("imageId") id: Int): Flow<Image>

    @POST("images/new")
    suspend fun insertImage(@Body image: Image)

    @PUT("image/edit")
    suspend fun updateImage(@Body image: Image)

    @DELETE("image/delete")
    suspend fun deleteImage(@Body image: Image)
}