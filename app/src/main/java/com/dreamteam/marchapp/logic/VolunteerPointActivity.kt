package com.dreamteam.marchapp.logic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dreamteam.marchapp.R

class VolunteerPointActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_point)

        val pointName = findViewById<TextView>(R.id.pointName)
        val pointBtn = findViewById<Button>(R.id.pointBtn)
        val backBtn = findViewById<Button>(R.id.btnBack)

        backBtn.setOnClickListener{
            val Intent = Intent(this, VolunteerMain::class.java)
            startActivity(Intent)
        }

        pointBtn.setOnClickListener{
            if (pointName.text.isNullOrBlank()) {
                Toast.makeText(this, "Musisz wybrać punkt!", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Punkt został przypisany!", Toast.LENGTH_SHORT).show()
                //TODO: tutaj będzie zapytanie do bazy odnośnie przypisania danego punktu do wolontariusza,
                //TODO: jeśli dany punkt istnieje, w przeciwnym wypadku komunikat o błędzie.
            }
        }


    }


}