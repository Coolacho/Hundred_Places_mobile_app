package com.example.hundredplaces.ui.camera

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_SENSOR
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.google.android.gms.vision.barcode.Barcode
import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE
import com.google.protobuf.ByteString
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.Executors

class CameraViewModel : ViewModel() {

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


    private val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .also {
            it.setAnalyzer(
                executor,
                MlKitAnalyzer(
                    listOf(barcodeScanner),
                    COORDINATE_SYSTEM_SENSOR,
                    executor
                ) { result: MlKitAnalyzer.Result? ->
                    val barcodeResults = result?.getValue(barcodeScanner)
                    if ((barcodeResults.isNullOrEmpty()) ||
                        (barcodeResults.first() == null)
                    ) {
                        return@MlKitAnalyzer
                    }

                    val barcode = barcodeResults[0]
                    Log.d("QR code scan", "Display: ${barcode.displayValue}; Url: ${barcode.url!!.url!!}")
                    when(barcode.valueType) {
                        Barcode.URL -> {
                            _uiState.update {
                                it.copy(
                                    qrContent = barcode.url!!.url!!.toUri()
                                )
                            }
                        }
                    }
                }
//                ImageAnalysis.Analyzer { imageProxy ->
//
//                    val buffer = imageProxy.planes[0].buffer
//                    val data = ByteString.copyFrom(buffer)
//
//                    //detectLandmarks(data)
//
//                    imageProxy.close()
//                }
            )
        }

    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        processCameraProvider.bindToLifecycle(
            lifecycleOwner, DEFAULT_BACK_CAMERA, cameraPreview, imageAnalysis
        )

        // Cancellation signals we're done with the camera
        try { awaitCancellation() } finally { processCameraProvider.unbindAll() }
    }

    private fun detectLandmarks(imgBytes: ByteString) {

        val image = Image.newBuilder().setContent(imgBytes).build()

        val feat = Feature.newBuilder()
            .setType(Feature.Type.LANDMARK_DETECTION)
            .build()

        val imageRequest = AnnotateImageRequest.newBuilder()
            .addFeatures(feat)
            .setImage(image)
            .build()

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        ImageAnnotatorClient.create().use { client ->
            val response = client.batchAnnotateImages(listOf(imageRequest))

            response.responsesList.forEach { res ->
                if (res.hasError()) {
                    Log.e("Landmark Detection", "${res.error.message}")
                    return
                }

                res.landmarkAnnotationsList.forEach { annotation ->
                    val info = annotation.locationsList.listIterator().next()
                    Log.d("Landmark Detection", "${annotation.description} ${info.latLng}")
                }
            }
        }
    }
}