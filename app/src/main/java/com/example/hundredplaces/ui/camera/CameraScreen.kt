package com.example.hundredplaces.ui.camera

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import androidx.camera.compose.CameraXViewfinder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hundredplaces.MainActivity
import com.example.hundredplaces.ui.navigation.NavigationDestination
import okhttp3.internal.toLongOrDefault

object CameraScreenDestination : NavigationDestination {
    override val route = "Camera Screen Destination"

}

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    cameraViewModel: CameraViewModel = CameraViewModel(),
) {
    val uiState = cameraViewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    LaunchedEffect(lifecycleOwner) {
        cameraViewModel.bindToCamera(context.applicationContext, lifecycleOwner)
    }

    when(uiState.qrContent) {
        null -> {
            uiState.surfaceRequest?.let { request ->
                CameraXViewfinder(
                    surfaceRequest = request,
                    modifier = modifier
                )
            }
        }
        else -> {
            val browserIntent = Intent(
                ACTION_VIEW,
                uiState.qrContent,
                context,
                MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra("navigateToPlacesDetails", true)
                .putExtra("placeId", uiState.qrContent.lastPathSegment?.toLongOrDefault(0L))
            context.startActivity(browserIntent)
        }
    }
}