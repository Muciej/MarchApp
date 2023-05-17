package com.dreamteam.marchapp.logic.participant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.JDBCConnector
import com.dreamteam.marchapp.logic.shared.LoginActivity
import com.dreamteam.marchapp.logic.validation.*

class ParticipantStatistics : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participant_statistics)

        val name = findViewById<TextView>(R.id.nameText)
        val lastname = findViewById<TextView>(R.id.lastnameText)
        val backBtn = findViewById<Button>(R.id.btnBack)

        /**
         * TODO: Kwerendy wpisujące dane jak imię i nazwisko
         */

        backBtn.setOnClickListener {
            val Intent = Intent(this, LoginActivity::class.java)
            startActivity(Intent)
        }

    }
}