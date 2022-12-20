package com.dreamteam.marchapp.logic.shared.stats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dreamteam.marchapp.R

class OverallMarchRes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overall_march_res)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            val Intent = Intent(this, MarchResults::class.java)
            startActivity(Intent)
        }
    }
}