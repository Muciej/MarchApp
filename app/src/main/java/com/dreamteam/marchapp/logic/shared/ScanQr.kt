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
import com.dreamteam.marchapp.database.DBConnector
import com.dreamteam.marchapp.database.JDBCConnector
import java.text.SimpleDateFormat
import java.util.*

class ScanQr : AppCompatActivity() {

    var connector: DBConnector = JDBCConnector
    private lateinit var codeScanner: CodeScanner

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        connector.startConnection()

        /**
         * currentUserID - it's id of 'volunteer' obtained from 'personel' table by using 'wolontariusze_info_view'
         * pointID - it's id of point which is related to 'volunteer'
         */
        var CurrentUserID = connector.getCurrentUserID()
        connector.prepareQuery("select* from wolontariusze_info_view where id_konta like '${CurrentUserID}' ")
        connector.executeQuery()
        var pointID = Integer.parseInt(connector.getCol(4)[0])
        connector.closeQuery()


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
                        var isInDatabase = true

                        // if qr code linked to user exists try to get info otherwise exception that doesnt exists
                        connector.startConnection()
                        connector.prepareQuery("SELECT * FROM uczestnicy WHERE kod_qr LIKE '${it.text}'; ")
                        connector.executeQuery()
                        try {
                            connector.getAnswer()
                        } catch(e: Exception){
                            isInDatabase = false
                        }

                        /**
                         * if user has qr code linked then add to uczestnik_punkt:
                         * startID - start ID which is linked with user
                         * pointID - id of the point which is connected with volunteer
                         * current android system date in sql 'datetime' type format -> YYYY-MM-DD HH:mm:ss
                         */
                        if(isInDatabase) {
                            Toast.makeText(this, "Scan result correct, id: ${it.text}", Toast.LENGTH_LONG).show()

                            connector.closeQuery()
                            try{
                                connector.prepareQuery("SELECT * FROM uczestnicy WHERE kod_qr LIKE '${it.text}'; ")
                                connector.executeQuery()
                            } catch (e : Exception){
                                Toast.makeText(this, "Błąd przy wyszukiwaniu użytkownika o danym ID z kodu QR", Toast.LENGTH_LONG).show()
                            }

                            // get startID from query above
                            var startID = Integer.parseInt(connector.getCol(1)[0])
                            connector.closeQuery()

                            // get sys date
                            var date = getCurrentDate()

                            // check whether this user was scanned on this point before
                            connector.prepareQuery("select * from uczestnik_punkt where id_uczestnika = ? and id_punktu = ? ;")
                            connector.setIntVar(startID, 1)
                            connector.setIntVar(pointID, 2)
                            var scanned = true
                            try {
                                connector.executeQuery()
                                connector.getAnswer()
                            } catch (e : Exception){
                                scanned = false
                            }

                            if(scanned){
                                Toast.makeText(this, "Użytkownik o id $startID już odwiedził ten punkt kontrolny", Toast.LENGTH_LONG).show()
                            } else {
                                // inserting informations into 'uczestnik_punkt' tabel
                                try{
                                    connector.prepareQuery("insert into uczestnik_punkt(id_uczestnika, id_punktu, data) values (${startID}, ${pointID}, '${date}' ) ")
                                    connector.executeQuery()
                                    connector.closeQuery()
                                } catch (e : Exception) {
                                    Toast.makeText(
                                        this,
                                        "Błąd przy aktualizowaniu bazy danych",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

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
        connector.closeConnection()
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