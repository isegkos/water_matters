package com.example.watermatters.ui

import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.material.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

@Composable
fun QRScannerScreen(onConfirm : (userName: String) -> Unit){
    val openDialog = remember { mutableStateOf(false) }
    val userName = remember {mutableStateOf("")}

    QRScanner(onQRCodeScanned = {barcode ->
        openDialog.value = true
        userName.value = barcode.rawValue.toString()
    })
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "@" + userName.value)
            },
            text = {
                Text(text = "Press Confirm to Drink Water!")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onConfirm(userName.value)
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@Composable
fun QRScanner(
    modifier: Modifier = Modifier,
    onQRCodeScanned : (barcode : Barcode) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier,
        factory = { context ->
            // Normally at MainActivity's setContent level...
            val cameraController = LifecycleCameraController(context)
            cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            val previewView = PreviewView(context).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
            val barcodeScanner = BarcodeScanning.getClient(options)
            val executor = ContextCompat.getMainExecutor(context)

            cameraController.setImageAnalysisAnalyzer(
                executor,
                MlKitAnalyzer(
                    listOf(barcodeScanner),
                    CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED,
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
                    else {
                        onQRCodeScanned(barcodeResults[0])
                    }

                    val qrCodeViewModel = QrCodeViewModel(barcodeResults[0])
                    val qrCodeDrawable = QrCodeDrawable(qrCodeViewModel)

                    previewView.overlay.clear()
                    previewView.overlay.add(qrCodeDrawable)
                }
            )

            cameraController.bindToLifecycle(lifecycleOwner)
            previewView.controller = cameraController

            previewView
        }
    )
}
