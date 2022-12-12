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

object CodeQr {
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
        //TODO zapytanie do bazy czy istnieje taki kod (narazie zhardcodowane)
        val stringsFromDb = arrayListOf("abcdefghij", "abplsadhij", "abAIFSJDNIij")
        for(s in stringsFromDb)
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
        //TODO code-> zamienić na zmienną zwracaną z bazy danych. Ta zmienna to ma być indeks
        // danego usera i on ma być wykorzystany jako nazwa pliku qr, czyli np. 1.jpg, 2.jpg,...,n (gdzie n - to liczba uczestnikow)
        // Czyli trzeba bedzie napisać query do bazy danych zwracające te pole
        qrgSaver.save(savePath, code, bitmap, QRGContents.ImageType.IMAGE_JPEG)
    }

    fun createCode() : String {
        var code = randomString()

        if(validateString(code)){
            code = randomString()
        }

        generateCodeActivity(code)

        return code
    }
}