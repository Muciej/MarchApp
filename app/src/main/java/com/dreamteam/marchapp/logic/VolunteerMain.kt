package com.dreamteam.marchapp.logic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.dreamteam.marchapp.R

class VolunteerMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_main)

        val statisticsBtn = findViewById<Button>(R.id.participantsStatistics)
        val pointBtn = findViewById<Button>(R.id.myPoint)
        val scannerBtn = findViewById<Button>(R.id.scanner)
        val changeBtn = findViewById<Button>(R.id.changePass)
        val logoutBtn = findViewById<Button>(R.id.logOut)

        statisticsBtn.setOnClickListener{
            Toast.makeText(this, "Tu będzie ekran podglądu statystyk uczestników", Toast.LENGTH_SHORT).show()
        }

        pointBtn.setOnClickListener{
            val Intent = Intent(this, VolunteerPointActivity::class.java)
            startActivity(Intent)
        }

        scannerBtn.setOnClickListener{
            val Intent = Intent(this, VolunteerScannerActivity::class.java)
            startActivity(Intent)
        }

        changeBtn.setOnClickListener{
            Toast.makeText(this, "Tu będzie ekran zmiany hasła", Toast.LENGTH_SHORT).show()
        }

        logoutBtn.setOnClickListener{
            val Intent = Intent(this, MainActivity::class.java)
            startActivity(Intent)
        }
    }


}