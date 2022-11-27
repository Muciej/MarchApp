package com.dreamteam.marchapp.logic.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.logic.shared.CreateUserActivity
import com.dreamteam.marchapp.logic.shared.MainActivity

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
            val intent = Intent(this, CreateVolunteerActivity::class.java)
            startActivity(intent)
        }

        crtParBtn.setOnClickListener{
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }

        updVolBtn.setOnClickListener{
            val intent = Intent(this, UpdateVolunteerData::class.java)
            startActivity(intent)
        }

        updParBtn.setOnClickListener{
            val intent = Intent(this, UpdateParticipantData::class.java)
            startActivity(intent)
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