package com.example.hundredplaces.data.repositories

import com.example.hundredplaces.data.services.landmark.LandmarkService
import com.example.hundredplaces.data.services.landmark.RequestResponse
import okhttp3.MultipartBody

class TestLandmarkService: LandmarkService {
    override suspend fun getLandmark(image: MultipartBody.Part): RequestResponse {
        TODO("Not yet implemented")
    }
}