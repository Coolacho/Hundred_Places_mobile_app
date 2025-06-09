package com.example.hundredplaces.data.services.landmark

import android.util.Log
import com.example.hundredplaces.util.NetworkConnection
import okhttp3.MultipartBody
import java.net.SocketTimeoutException

class LandmarkServiceImpl(
    private val landmarkRestApi: LandmarkRestApi,
    private val networkConnection: NetworkConnection
) : LandmarkService {
    override suspend fun getLandmark(image: MultipartBody.Part): RequestResponse {
        if (networkConnection.isNetworkConnected) {
            try {
                val result = landmarkRestApi.getLandmark(image)
                if (result.isSuccessful && result.body() != null) {
                    return result.body()!!
                }
            }
            catch (_: SocketTimeoutException) {
                Log.e("Landmark service", "Server timeout")
            }
        }
        return RequestResponse(success = false, landmark = null)
    }
}