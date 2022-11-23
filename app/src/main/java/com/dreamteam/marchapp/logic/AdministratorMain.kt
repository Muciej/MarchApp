package com.dreamteam.marchapp.logic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dreamteam.marchapp.R

class AdministratorMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrator_main)

        val crtVolBtn = findViewById<Button>(R.id.createVolunteer)
        val crtParBtn = findViewById<Button>(R.id.createParticipant)
        val updVolBtn = findViewById<Button>(R.id.updateVolunteer)
        val updParBtn = findViewById<Button>(R.id.updateParticipant)
        val changeBtn = findViewById<Button>(R.id.changePass)
        val logoutBtn = findViewById<Button>(R.id.logOut)

        crtVolBtn.setOnClickListener{
            val Intent = Intent(this, CreateVolunteerActivity::class.java)
            startActivity(Intent)
        }

        crtParBtn.setOnClickListener{
            Toast.makeText(this, "Tu będzie ekran tworzenia konta uczestnika", Toast.LENGTH_SHORT).show()
        }

        updVolBtn.setOnClickListener{
            Toast.makeText(this, "Tu będzie ekran modyfikacji konta wolontariusza", Toast.LENGTH_SHORT).show()
        }

        updParBtn.setOnClickListener{
            Toast.makeText(this, "Tu będzie ekran modyfikacji konta uczestnika", Toast.LENGTH_SHORT).show()
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