package com.example.hundredplaces.data.model.visit

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime

const val baseUrl = "visits"
interface VisitRestApiService {
    @GET("$baseUrl/all")
    suspend fun getAllVisitDatesByUserIdAndPlaceId(
        @Query("userId") userId: Long,
        @Query("placeId") placeId: Long
    ): List<LocalDateTime>

    @POST("$baseUrl/new")
    suspend fun insertVisit(@Body visit: Visit)

}