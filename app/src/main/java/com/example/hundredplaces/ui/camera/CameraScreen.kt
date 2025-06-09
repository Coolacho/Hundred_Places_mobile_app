package com.example.hundredplaces.ui.camera

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.camera.compose.CameraXViewfinder
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.components.LoadingScreen
import com.example.hundredplaces.ui.navigation.MenuNavigationDestination
import kotlinx.coroutines.delay
import androidx.core.net.toUri
import com.example.hundredplaces.ui.AppViewModelProvider

object CameraScreenDestination : MenuNavigationDestination {
    override val route = "Camera Screen Destination"
    override val title = R.string.camera_screen_title
    override val iconRes =R.drawable.rounded_qr_code_scanner_24
    const val USE_CASE_ARG = "useCase"
    val routeWithArgs = "$route/{$USE_CASE_ARG}"
}

enum class CameraUseCaseEnum {
    QR_CODE,
    LANDMARK
}

@Composable
fun CameraScreen(
    navigateToPlace: (Long, Boolean) -> Unit,
    useCase: CameraUseCaseEnum,
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    cameraViewModel: CameraViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState = cameraViewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    LaunchedEffect(lifecycleOwner) {
        cameraViewModel.bindToCamera(context.applicationContext, lifecycleOwner, useCase)
    }

    BackHandler(
        enabled = uiState.image != null && useCase == CameraUseCaseEnum.LANDMARK
    ) {
        cameraViewModel.imageShown()
    }

    uiState.qrContent?.lastPathSegment?.let {
        if (useCase == CameraUseCaseEnum.QR_CODE) {
            LaunchedEffect(it) {
                navigateToPlace(it.toLong(), true)
                cameraViewModel.qrContentRead()
            }
        }
    }

    uiState.image?.let {
        if (useCase == CameraUseCaseEnum.LANDMARK) {
            ImagePreviewScreen(
                onDiscardClick = cameraViewModel::imageShown,
                onSubmitClick = cameraViewModel::submitPicture,
                image = it.asImageBitmap(),
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1.1f)
            )
        }
    }

    uiState.requestResponse?.let {
        if (useCase == CameraUseCaseEnum.LANDMARK) {
            LandmarkAlertDialog(
                requestResponse = it,
                placeId = uiState.placeId,
                onDismissRequest = cameraViewModel::requestResultShown,
                navigateToPlace = { placeId, addVisit ->
                    navigateToPlace(placeId, addVisit)
                    cameraViewModel.requestResultShown()
                },
                searchWeb = { query->
                    searchWeb(query, context)
                    cameraViewModel.requestResultShown()
                }
            )
        }
    }

    if (uiState.isLoading) {
        LoadingScreen(
            alpha = 0.65f,
            modifier = modifier
                .zIndex(1.3f)
        )
    }

    if (uiState.isInfoWindowOpen) {
        LaunchedEffect(Unit) {
            delay(5000)
            cameraViewModel.toggleInfoWindow()
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
            CameraInfoWindow(
                text = stringResource(if (useCase == CameraUseCaseEnum.QR_CODE) R.string.camera_qr_info else R.string.camera_landmark_info),
                visible = uiState.isInfoWindowOpen,
                modifier = Modifier
                    .padding(
                        end = 42.dp,
                        bottom = 45.dp
                    )
                    .zIndex(1f)
            )
            IconButton(
                onClick = cameraViewModel::toggleInfoWindow,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                Icon(
                    painter = painterResource(R.drawable.round_info_outline_24),
                    contentDescription = "Info button",
                    tint = Color.White
                )
            }
            if (useCase == CameraUseCaseEnum.QR_CODE) {
                QrCodeBox(
                    modifier = Modifier
                        .size(240.dp)
                        .align(Alignment.Center)
                )
            }
            if (useCase == CameraUseCaseEnum.LANDMARK) {
                IconButton(
                    onClick = cameraViewModel::takePicture,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .size(72.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        .border(4.dp, Color.LightGray, CircleShape)
                        .align(Alignment.BottomCenter)
                ) {}
            }
        }
    }
}

@Composable
fun CameraInfoWindow(
    text: String,
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.shapes.small
                )
                .width(275.dp)
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
            )
        }
    }
}

@Composable
fun QrCodeBox(
    modifier: Modifier = Modifier
)
{
    Canvas(
        modifier = modifier
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

fun searchWeb(query: String, context: Context) {
    val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
        putExtra(SearchManager.QUERY, query)
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
    else {
        val fallback = Intent(Intent.ACTION_VIEW, "https://www.google.com/search?q=$query".toUri())
        context.startActivity(fallback)
    }
}