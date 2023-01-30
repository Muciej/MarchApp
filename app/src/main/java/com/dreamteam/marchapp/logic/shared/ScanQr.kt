package com.dreamteam.marchapp.logic.shared
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.budiyev.android.codescanner.*
import com.dreamteam.marchapp.R
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import com.dreamteam.marchapp.database.DBConnector
import com.dreamteam.marchapp.database.JDBCConnector
import java.util.*

class ScanQr : AppCompatActivity() {

    var connector: DBConnector = JDBCConnector
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {

        connector.startConnection()

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

                // Callbacks
                codeScanner.decodeCallback = DecodeCallback {
                    runOnUiThread {
                        var isInDatabase = true
                        connector.prepareQuery("SELECT kod_qr FROM uczestnicy WHERE kod_qr LIKE '${it.text}' ")
                        connector.executeQuery()
                        try {
                            var answer: Vector<Vector<String>> = connector.getAnswer()
                        } catch(e: Exception){
                            isInDatabase = false
                        }

                        if(isInDatabase) {
                            Toast.makeText(this, "Scan result correct, id: ${it.text}", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "No match in database, ? ${it.text}", Toast.LENGTH_LONG).show()
                        }

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

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}