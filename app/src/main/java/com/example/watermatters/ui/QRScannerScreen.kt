package com.example.watermatters.ui

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.impl.utils.ContextUtil
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

@Composable
fun QRScannerScreen(
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    // Normally at MainActivity's level...
    lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    lateinit var barcodeScanner: BarcodeScanner

    AndroidView(
        modifier = modifier,
        factory = { context ->
            // Normally at MainActivity's setContent level...
            cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            val previewView = PreviewView(context).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            cameraProviderFuture.addListener(Runnable
                {
                val cameraProvider = cameraProviderFuture.get()

                val preview : Preview = Preview.Builder()
                    .build()
                preview.setSurfaceProvider(previewView.surfaceProvider)

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                    .build()
                barcodeScanner = BarcodeScanning.getClient(options)

                val executor = ContextCompat.getMainExecutor(context)
                imageAnalysis.setAnalyzer(
                    executor,
                    MlKitAnalyzer(
                        listOf(barcodeScanner),
                        COORDINATE_SYSTEM_VIEW_REFERENCED,
                        executor
                    ) { result: MlKitAnalyzer.Result? ->
                        val barcodeResults = result?.getValue(barcodeScanner)
                        if ((barcodeResults == null) ||
                            (barcodeResults.size == 0) ||
                            (barcodeResults.first() == null)
                        ) {
                            previewView.overlay.clear()
                            previewView.setOnTouchListener { _, _ -> false } //no-op
                            return@MlKitAnalyzer
                        }

                        val qrCodeViewModel = QrCodeViewModel(barcodeResults[0])
                        val qrCodeDrawable = QrCodeDrawable(qrCodeViewModel)

                        previewView.setOnTouchListener(qrCodeViewModel.qrCodeTouchCallback)
                        previewView.overlay.clear()
                        previewView.overlay.add(qrCodeDrawable)
                    }
                )
                val cameraSelector : CameraSelector = CameraSelector.Builder()
//                    .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
                } catch(exc: Exception) {
                    Log.e("CAMERA-BINDING", "Use case binding failed", exc)
                }

                preview.setSurfaceProvider(previewView.surfaceProvider)
                },
                ContextCompat.getMainExecutor(context)
            )

            previewView
        }
    )
}
