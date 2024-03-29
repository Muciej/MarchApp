package com.dreamteam.marchapp.logic.organiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dreamteam.marchapp.R

class ModifyParticipantsData : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_participants_data)

        val btn_back = findViewById<Button>(R.id.btnForward)
        btn_back.setOnClickListener{
            val Intent = Intent(this, OrganisatorModifyEventMenu::class.java)
            startActivity(Intent)
        }
    }
}