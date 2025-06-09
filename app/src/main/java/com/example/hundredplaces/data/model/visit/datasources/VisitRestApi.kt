package com.example.hundredplaces.data.model.visit.datasources

import com.example.hundredplaces.data.model.visit.Visit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

const val baseUrl = "/api/v1/visits"
interface VisitRestApi {
    @GET(baseUrl)
    suspend fun getAllVisitsByUserId(@Query("userId") userId: Long): List<Visit>

    @POST("$baseUrl/new")
    suspend fun insertAll(@Body visits: List<Visit>)

}