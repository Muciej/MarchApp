package com.dreamteam.marchapp.logic.organiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.dreamteam.marchapp.R

class ViewStAsOrganiser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_st_as_organiser)

        val btnForward = findViewById<Button>(R.id.btnForward)
        val Stat_by_point = findViewById<Button>(R.id.Stat_by_point)
        val overall_stats = findViewById<Button>(R.id.overall_stats)
        val participantsStatistics = findViewById<Button>(R.id.participantsStatistics)

        btnForward.setOnClickListener {
            val Intent = Intent(this, OrganisatorMain::class.java)
            startActivity(Intent)
        }

        //TODO: W późniejszych wersjach aplikacji poniższe przyciski będą przenosić
        //TODO: użytkownika do ekranów ze statystykami, które będą pobierać statystyki
        //TODO: za pomocą odpowiednich kwerend.

        Stat_by_point.setOnClickListener {
            val intent = Intent(this, SelectPointToFollow::class.java)
            startActivity(intent)
        }

        overall_stats.setOnClickListener {
            Toast.makeText(
                this,
                "To będzie podgląd ogólnych statystyk!",
                Toast.LENGTH_SHORT
            ).show()
        }

        participantsStatistics.setOnClickListener {
            Toast.makeText(
                this,
                "To będzie podgląd statystyk uczestników!",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}