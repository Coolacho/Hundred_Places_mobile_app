package com.example.hundredplaces.ui.camera

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import androidx.camera.compose.CameraXViewfinder
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hundredplaces.MainActivity
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.navigation.NavigationDestination
import kotlinx.coroutines.delay
import okhttp3.internal.toLongOrDefault

object CameraScreenDestination : NavigationDestination {
    override val route = "Camera Screen Destination"

}

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    cameraViewModel: CameraViewModel = viewModel()
) {
    val uiState = cameraViewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    LaunchedEffect(lifecycleOwner) {
        cameraViewModel.bindToCamera(context.applicationContext, lifecycleOwner)
    }

    if (uiState.qrContent != null) {
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

    if (uiState.isInfoScreenOpen) {
        LaunchedEffect(Unit) {
            delay(5000)
            cameraViewModel.toggleInfoScreen()
        }
    }

    uiState.surfaceRequest?.let { request ->
        CameraXViewfinder(
            surfaceRequest = request,
            modifier = modifier
                .fillMaxSize()
        )
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = uiState.isInfoScreenOpen
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(
                            end = 55.dp,
                            bottom = 24.dp
                        )
                        .background(
                            Color.LightGray.copy(alpha = 0.5f),
                            MaterialTheme.shapes.small
                        )
                        .width(275.dp)
                ) {
                    Text(
                        text = stringResource(R.string.camera_info_text),
                        textAlign = TextAlign.Justify,
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                }
            }
            IconButton(
                onClick = cameraViewModel::toggleInfoScreen,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                Icon(
                    painter = painterResource(R.drawable.round_info_outline_24),
                    contentDescription = "Info button",
                    tint = Color.White
                )
            }
            Canvas(
                modifier = Modifier
                    .size(240.dp)
                    .align(Alignment.Center)
            ) {
                val cornerLength = 40.dp.toPx() // Corner line length
                val strokeWidth = 5.dp.toPx() // Stroke thickness
                val color = Color.White // Corner color

                val capStyle = StrokeCap.Round // Makes the corners round

                // Top-left corner
                drawLine(color, Offset(0f, 0f), Offset(cornerLength, 0f), strokeWidth, capStyle)
                drawLine(color, Offset(0f, 0f), Offset(0f, cornerLength), strokeWidth, capStyle)

                // Top-right corner
                drawLine(color, Offset(size.width, 0f), Offset(size.width - cornerLength, 0f), strokeWidth, capStyle)
                drawLine(color, Offset(size.width, 0f), Offset(size.width, cornerLength), strokeWidth, capStyle)

                // Bottom-left corner
                drawLine(color, Offset(0f, size.height), Offset(cornerLength, size.height), strokeWidth, capStyle)
                drawLine(color, Offset(0f, size.height), Offset(0f, size.height - cornerLength), strokeWidth, capStyle)

                // Bottom-right corner
                drawLine(color, Offset(size.width, size.height), Offset(size.width - cornerLength, size.height), strokeWidth, capStyle)
                drawLine(color, Offset(size.width, size.height), Offset(size.width, size.height - cornerLength), strokeWidth, capStyle)
            }
        }
    }
}