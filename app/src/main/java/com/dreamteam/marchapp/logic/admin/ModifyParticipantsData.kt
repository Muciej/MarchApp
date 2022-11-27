package com.dreamteam.marchapp.logic.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.logic.organiser.Organisatormain2

class ModifyParticipantsData : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_participants_data)

        val btn_back = findViewById<Button>(R.id.btnForward)
        btn_back.setOnClickListener{
            val Intent = Intent(this, Organisatormain2::class.java)
            startActivity(Intent)
        }
    }
}