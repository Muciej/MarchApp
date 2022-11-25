package com.dreamteam.marchapp.logic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dreamteam.marchapp.R

class VolunteerScannerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_scanner)

        val participantId = findViewById<TextView>(R.id.participantId)
        val scanBtn = findViewById<Button>(R.id.scanBtn)
        val backBtn = findViewById<Button>(R.id.btnBack)

        backBtn.setOnClickListener{
            val Intent = Intent(this, VolunteerMain::class.java)
            startActivity(Intent)
        }

        scanBtn.setOnClickListener{
            if (participantId.text.isNullOrBlank()) {
                Toast.makeText(this, "Musisz wpisać id!", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Uczestnik został prawidłowo przeskanowany!", Toast.LENGTH_SHORT).show()
                //TODO: tutaj będzie zapytanie do bazy odnośnie wpisania do bazy że uczesnik o podanym id
                //TODO: odwiedził ten punkt
                //TODO: jeśli uczestnik o podanym id nie istnieje -  komunikat o błędzie.
            }
        }
    }
}