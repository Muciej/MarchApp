package com.dreamteam.marchapp.logic.shared.stats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dreamteam.marchapp.R

class ListOfParWhoPassedAGivenPoint : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_par_who_passed_agiven_point)

        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            val Intent = Intent(this, ParWhoPassedAGivenPoint::class.java)
            startActivity(Intent)
        }
    }
}