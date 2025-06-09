package com.example.hundredplaces.data.services.landmark

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface LandmarkRestApi {
    @Multipart
    @POST("/api/v1/landmark")
    suspend fun getLandmark(@Part image: MultipartBody.Part): Response<RequestResponse>
}