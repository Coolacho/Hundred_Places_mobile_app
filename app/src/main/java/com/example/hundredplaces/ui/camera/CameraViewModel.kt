package com.example.hundredplaces.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_SENSOR
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.place.repositories.PlaceRepository
import com.example.hundredplaces.data.services.landmark.LandmarkService
import com.google.android.gms.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

class CameraViewModel(
    private val landmarkService: LandmarkService,
    private val placeRepository: PlaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    private val executor = Executors.newSingleThreadExecutor()

    private val cameraPreview = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _uiState.update {
                it.copy(
                    surfaceRequest = newSurfaceRequest
                )
            }
        }
    }

    private val barcodeScannerOptions = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(FORMAT_QR_CODE)
        .build()
    private val barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions)

    private val mlKitAnalyzer = MlKitAnalyzer(
        listOf(barcodeScanner),
        COORDINATE_SYSTEM_SENSOR,
        executor
    ) { result: MlKitAnalyzer.Result? -> analyzeQrCode(result) }

    private val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .also {
            it.setAnalyzer( executor, mlKitAnalyzer )
        }

    private val imageCapture = ImageCapture.Builder()
        .build()

    private fun analyzeQrCode(result: MlKitAnalyzer.Result?) {
        val barcodeResults = result?.getValue(barcodeScanner)
        if ((barcodeResults.isNullOrEmpty()) ||
            (barcodeResults.first() == null)
        ) {
            return
        }

        val barcode = barcodeResults[0]
        when(barcode.valueType) {
            Barcode.URL -> {
                val pattern = Regex("""http://192\.168\.2\.150:8080/places/\d+$""")
                val url = barcode.url?.url
                if (url != null && pattern.matches(url)) {
                    _uiState.update {
                        it.copy(
                            qrContent = barcode.url!!.url!!.toUri()
                        )
                    }
                }
            }
        }
    }

    private fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix().apply {
            postRotate(angle)
        }
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner, useCase: CameraUseCaseEnum) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        when (useCase) {
            CameraUseCaseEnum.QR_CODE -> processCameraProvider.bindToLifecycle(lifecycleOwner, DEFAULT_BACK_CAMERA, cameraPreview, imageAnalysis)
            CameraUseCaseEnum.LANDMARK -> processCameraProvider.bindToLifecycle(lifecycleOwner, DEFAULT_BACK_CAMERA, cameraPreview, imageCapture)
        }

        // Cancellation signals we're done with the camera
        try { awaitCancellation() } finally { processCameraProvider.unbindAll() }
    }

    fun takePicture() {
        imageCapture.takePicture(executor,
            object: ImageCapture.OnImageCapturedCallback() {
                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Image Capture", exception.message.toString())
                }

                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    super.onCaptureSuccess(imageProxy)

                    //Get image bitmap from proxy and rotate it to match preview
                    val bitmap = rotateBitmap(imageProxy.toBitmap(), imageProxy.imageInfo.rotationDegrees.toFloat())

                    _uiState.update {
                        it.copy(
                            image = bitmap
                        )
                    }

                    imageProxy.close()
                    Log.d("Image Capture", "Image captured.")
                }
            })

    }

    fun submitPicture() {

        //Convert to bitmap to bytearray
        val bos = ByteArrayOutputStream()
        _uiState.value.image?.compress(Bitmap.CompressFormat.JPEG, 90, bos)
        val byteArray = bos.toByteArray()

        //Create multipart body for api request
        val requestFile = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)

        _uiState.update {
            it.copy(
                isLoading = true
            )
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val requestResult = landmarkService.getLandmark(multipartBody)
                val landmark = if (requestResult.success) requestResult.landmark else null
                val placeId = if (landmark != null) placeRepository.findPlaceByCoordinates(landmark.latitude, landmark.longitude) else null

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        requestResponse = requestResult,
                        placeId = placeId
                    )
                }
            }
        }
    }

    fun qrContentRead() {
        _uiState.update {
            it.copy(
                qrContent = null
            )
        }
    }

    fun imageShown() {
        _uiState.update {
            it.copy(
                image = null
            )
        }

    }

    fun requestResultShown() {
        _uiState.update {
            it.copy(
                requestResponse = null
            )
        }
        imageShown()
    }

    fun toggleInfoWindow() {
        _uiState.update {
            it.copy(
                isInfoWindowOpen = !it.isInfoWindowOpen
            )
        }
    }
}