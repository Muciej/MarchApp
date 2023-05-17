package com.dreamteam.marchapp.logic.organiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.dreamteam.marchapp.R

class ModifyTracks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_tracks)

        val btnBack = findViewById<Button>(R.id.btn_back)
        val uploadMap = findViewById<Button>(R.id.upload_map)
        val assignPoint = findViewById<Button>(R.id.assign_point)

        btnBack.setOnClickListener{
            val Intent = Intent(this, OrganisatorModifyEventMenu::class.java)
            startActivity(Intent)
        }


        uploadMap.setOnClickListener {
            Toast.makeText(this, "Tu pojawi się możliwość wyboru mapy", Toast.LENGTH_SHORT)
                .show()
        }

        assignPoint.setOnClickListener {
            Toast.makeText(this, "Tu pojawi się możliwość przypisanie punktów na mapie", Toast.LENGTH_SHORT)
                .show()
        }
    }
}