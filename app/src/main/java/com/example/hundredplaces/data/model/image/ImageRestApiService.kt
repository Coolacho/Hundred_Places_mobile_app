package com.example.hundredplaces.data.model.image

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

const val baseUrl = "images"
interface ImageRestApiService {
    @GET("$baseUrl/all")
    suspend fun getAllImages(): List<Image>

    @GET("$baseUrl/image/{imageId}")
    suspend fun getImage(@Path("imageId") id: Long): Image

    @POST("$baseUrl/new")
    suspend fun insertImage(@Body image: Image)

    @PUT("$baseUrl/update")
    suspend fun updateImage(@Body image: Image)

    @DELETE("$baseUrl/delete")
    suspend fun deleteImage(@Body image: Image)
}