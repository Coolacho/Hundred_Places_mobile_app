package com.example.hundredplaces.ui.camera

import android.net.Uri
import androidx.camera.core.SurfaceRequest

data class CameraUiState (
    // Used to set up a link between the Camera and your UI.
    val surfaceRequest: SurfaceRequest? = null,
    val qrContent: Uri? = null
)
