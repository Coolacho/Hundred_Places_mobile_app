package com.example.hundredplaces.ui.camera

import android.graphics.Bitmap
import android.net.Uri
import androidx.camera.core.SurfaceRequest
import com.example.hundredplaces.data.services.landmark.RequestResponse

data class CameraUiState (
    // Used to set up a link between the Camera and your UI.
    val surfaceRequest: SurfaceRequest? = null,
    val qrContent: Uri? = null,
    val image: Bitmap? = null,
    val requestResponse: RequestResponse? = null,
    val placeId: Long? = null,
    val isLoading: Boolean = false,
    val isInfoWindowOpen: Boolean = true
)
