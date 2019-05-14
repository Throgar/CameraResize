package com.example.jakub.cameraresize

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.*
import android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Surface
import android.view.SurfaceView

class Scanner : AppCompatActivity() {

    var preview: SurfaceView? = null
    var cameraDevice: CameraDevice? = null
    var cameraCaptureSession : CameraCaptureSession? = null
    var requestBuilder : CaptureRequest.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        preview = findViewById(R.id.surface)

        openCamera()

    }

    val stateCallBack: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice?.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice?.close()
            cameraDevice = null
        }


    }

    fun openCamera() {

        val manager: CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = manager.cameraIdList[0]

        try {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 200)
            }
            manager.openCamera(cameraId, stateCallBack, preview?.handler)
        } catch (e : CameraAccessException) {
            e.printStackTrace()
        }

    }

    fun createCameraPreview() {
        requestBuilder = cameraDevice!!.createCaptureRequest(TEMPLATE_PREVIEW)
        val surface : Surface = preview?.holder!!.surface
        requestBuilder?.addTarget(surface)

        cameraDevice!!.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
            override fun onConfigureFailed(session: CameraCaptureSession) {

            }

            override fun onConfigured(session: CameraCaptureSession) {
                if(cameraDevice == null) {
                    return
                }

                cameraCaptureSession = session
                updatePreview()
            }
        }, null)

    }

    fun updatePreview() {
        requestBuilder?.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO)

        cameraCaptureSession?.setRepeatingBurst(listOf(requestBuilder?.build()), null, preview?.handler)
    }

}

