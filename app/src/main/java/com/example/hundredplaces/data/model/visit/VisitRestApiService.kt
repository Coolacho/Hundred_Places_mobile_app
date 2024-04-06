package com.example.hundredplaces.data.model.visit

import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VisitRestApiService {
    @GET("visits")
    suspend fun getAllVisits(): Flow<List<Visit>>

    @GET("visits/{visitId}")
    suspend fun getVisit(@Path("visitId") id: Int): Flow<Visit>

    @POST("visits/new")
    suspend fun insertVisit(@Body visit: Visit)

    @PUT("visit/edit")
    suspend fun updateVisit(@Body visit: Visit)

    @DELETE("visit/delete")
    suspend fun deleteVisit(@Body visit: Visit)
}