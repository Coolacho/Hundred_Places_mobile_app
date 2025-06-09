package com.example.hundredplaces.data.services.landmark

import okhttp3.MultipartBody

interface LandmarkService {
    suspend fun getLandmark(image: MultipartBody.Part): RequestResponse
}