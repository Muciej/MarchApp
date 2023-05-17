package com.dreamteam.marchapp.logic.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.JDBCConnector
import com.dreamteam.marchapp.logic.organiser.ShowVolunteers
import com.dreamteam.marchapp.logic.shared.ChangePassword
import com.dreamteam.marchapp.logic.shared.ChooseMarchActivity
import com.dreamteam.marchapp.logic.shared.ViewSt
import com.dreamteam.marchapp.logic.shared.ShowAndEditParticipant

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
        val viewStats = findViewById<Button>(R.id.view_statistics)
        val addPointToVolunteer = findViewById<Button>(R.id.addPointToVolunteer)

        crtVolBtn.setOnClickListener{
            val intent = Intent(this, CreateVolunteerActivity::class.java)
            startActivity(intent)
        }

        crtParBtn.setOnClickListener{
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }

        addPointToVolunteer.setOnClickListener{
            val intent = Intent(this, AssignVolunteerToPoint::class.java)
            startActivity(intent)
        }

        updVolBtn.setOnClickListener{
            val intent = Intent(this, ShowVolunteers::class.java)
            intent.putExtra("accessLevel", "Admin")
            startActivity(intent)
        }

        updParBtn.setOnClickListener{
            //val intent = Intent(this, UpdateParticipantData::class.java)
            val intent = Intent(this, ShowAndEditParticipant::class.java)
            intent.putExtra("accessLevel", "Admin")
            startActivity(intent)
        }

        changeBtn.setOnClickListener{
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }

        logoutBtn.setOnClickListener{
            JDBCConnector.closeConnection()
            val Intent = Intent(this, ChooseMarchActivity::class.java)
            startActivity(Intent)
        }

        viewStats.setOnClickListener{
            val Intent = Intent(this, ViewSt::class.java)
            startActivity(Intent)
        }
    }
}