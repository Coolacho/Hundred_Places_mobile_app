package com.example.hundredplaces.fakedata

import com.example.hundredplaces.data.model.visit.VisitsRestApi
import com.example.hundredplaces.data.model.visit.datasources.VisitsRemoteDataSource
import com.example.hundredplaces.util.InstantConverter
import com.google.gson.GsonBuilder
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

class VisitsRemoteDataSourceTest {

    private val baseUrl =
        "http://192.168.2.150:8080"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(
            GsonBuilder()
                .registerTypeAdapter(
                    LocalDateTime::class.java,
                    InstantConverter()
                ).create()))
        .build()
    private val dataSource = VisitsRemoteDataSource(
        visitsRestApi = retrofit.create(VisitsRestApi::class.java)
    )

    @Test
    fun visitsRemoteDataSource_getAllVisitsByUserId_verifyVisits() =
        runTest{
            println(dataSource.getAllVisitsByUserId(1))
        }
}