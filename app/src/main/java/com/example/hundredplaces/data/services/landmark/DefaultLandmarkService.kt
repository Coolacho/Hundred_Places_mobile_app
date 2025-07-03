package com.example.hundredplaces.data.services.landmark

import android.util.Log
import com.example.hundredplaces.util.NetworkMonitor
import okhttp3.MultipartBody
import java.net.SocketTimeoutException

class DefaultLandmarkService(
    private val landmarkRestApi: LandmarkRestApi,
    private val networkConnection: NetworkMonitor
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
        return RequestResponse(isSuccess = false, landmark = null)
    }
}