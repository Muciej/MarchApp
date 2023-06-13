package com.dreamteam.marchapp.logic.shared
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.budiyev.android.codescanner.*
import com.dreamteam.marchapp.R
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.dreamteam.marchapp.database.DataViewModel
import androidx.lifecycle.Observer
import com.dreamteam.marchapp.database.dataclasses.CheckPoint
import com.dreamteam.marchapp.database.dataclasses.Participant
import com.dreamteam.marchapp.database.dataclasses.Volounteer
import java.text.SimpleDateFormat
import java.util.*

class ScanQr : AppCompatActivity() {

//    var connector: DBConnector = JDBCConnector
    private lateinit var codeScanner: CodeScanner
    private lateinit var dataViewModel: DataViewModel
    private var volunteer: Volounteer? = null
    private var decodedQR: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        dataViewModel.loggedAcount.observe(this, Observer {
                loggedAccount -> {
                volunteer = dataViewModel.getVolounteer(loggedAccount)
            }
        })


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1);
            } else {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_camera_scan_qrcode)
                val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
                codeScanner = CodeScanner(this, scannerView)

                // Parameters (default values)
                codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
                codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
                // ex. listOf(BarcodeFormat.QR_CODE)
                codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
                codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
                codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
                codeScanner.isFlashEnabled = false // Whether to enable flash or not

                // Callbacks and DB operations on user account
                codeScanner.decodeCallback = DecodeCallback {
                    runOnUiThread {
                        Toast.makeText(this, "Scan result correct, id: ${it.text}", Toast.LENGTH_LONG).show()
                        decodedQR = it.text
                        qrCheckCallback(dataViewModel.getParticipantByQR(decodedQR))
                    }
                }
                codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                    runOnUiThread {
                        Toast.makeText(this, "Camera initialization error: ${it.message}",
                            Toast.LENGTH_LONG).show()
                    }
                }
                scannerView.setOnClickListener {
                    codeScanner.startPreview()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun qrCheckCallback(participant: Participant?){
        dataViewModel.markPointReached(volunteer?.pointId, participant, getCurrentDate())
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    @SuppressLint("WeekBasedYear")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCurrentDate(): String {
        val currentTime: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("YYYY-MM-DD HH:mm:ss", Locale.getDefault()).format(Date())
        } else {
            TODO("VERSION.SDK_INT < N")
        }

        return currentTime
    }
}