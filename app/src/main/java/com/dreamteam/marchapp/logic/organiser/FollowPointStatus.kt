package com.dreamteam.marchapp.logic.organiser

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dreamteam.marchapp.R

class FollowPointStatus: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_point_status)

        val backBtn = findViewById<Button>(R.id.btnBack)

        backBtn.setOnClickListener {
            val intent = Intent(this, SelectPointToFollow::class.java)
            startActivity(intent)
        }

        //TODO: Add next fields after making the database work
    }
}