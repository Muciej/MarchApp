package com.dreamteam.marchapp.logic.shared

import android.content.ContentValues.TAG
import android.graphics.Color
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import com.google.zxing.WriterException
import android.graphics.Bitmap
import android.os.Environment
import android.os.Build
import com.dreamteam.marchapp.database.DBConnector
import com.dreamteam.marchapp.database.JDBCConnector

object CodeQr {
    var connector: DBConnector = JDBCConnector
    private var STRING_LENGTH = 10
    private var savePath = Environment.getExternalStorageDirectory().path + "/QRCode/"


    private fun randomString() : String{
        val alphabet = "abcdefghijklmnopqrstuvwxyzABCD123456789"
        var randomStr = ""
        for(i in 1..STRING_LENGTH)
        {
            randomStr += alphabet.random()
        }
        return randomStr
    }

    private fun validateString(str : String) : Boolean {
        connector.prepareQuery("select kod_qr from uczestnicy")
        connector.executeQuery()
        val vectorFromDB = connector.getCol(1)
        connector.closeQuery()

        for(s in vectorFromDB)
        {
            if(str.equals(s))
            {
                return true
            }
        }
        return false
    }

    private fun generateCodeActivity(code:String) {

        //var manager = getSystemService(WINDOW_SERVICE)
        val width = 2000
        //var height = 100
        val smallerDimension = width
        lateinit var bitmap: Bitmap

        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        val qrgEncoder = QRGEncoder(code, null, QRGContents.Type.TEXT, smallerDimension)
        qrgEncoder.colorBlack = Color.WHITE
        qrgEncoder.colorWhite = Color.BLACK
        try {
            bitmap = qrgEncoder.bitmap
        } catch (e: WriterException) {
            Log.v(TAG, e.toString())
        }

        val qrgSaver = QRGSaver()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/QRCode/"
                }
        qrgSaver.save(savePath, code, bitmap, QRGContents.ImageType.IMAGE_JPEG)
    }

    fun createCode() : String {
        connector.startConnection()
        var code = randomString()

        while (validateString(code)) {
                code = randomString()
        }
        generateCodeActivity(code)
        connector.closeConnection()

        return code
    }
}