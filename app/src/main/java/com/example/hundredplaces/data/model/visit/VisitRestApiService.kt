package com.example.hundredplaces.data.model.visit

import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

const val baseUrl = "/api/v1/visits"
interface VisitRestApiService {
    @GET("$baseUrl/all")
    suspend fun getAllVisits(): Flow<List<Visit>>

    @GET("$baseUrl/visit/{visitId}")
    suspend fun getVisit(@Path("visitId") id: Long): Flow<Visit>

    @POST("$baseUrl/new")
    suspend fun insertVisit(@Body visit: Visit)

    @PUT("$baseUrl/update")
    suspend fun updateVisit(@Body visit: Visit)

    @DELETE("$baseUrl/delete")
    suspend fun deleteVisit(@Body visit: Visit)
}