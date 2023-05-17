package com.dreamteam.marchapp.logic.shared

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.JDBCConnector
import com.dreamteam.marchapp.logic.shared.stats.ChoosePointToViewStat
import com.dreamteam.marchapp.logic.shared.stats.MarchResults
import com.dreamteam.marchapp.logic.shared.stats.PieChartParWhoPassedAGivenPoint

class ViewSt : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_st_as_organiser)

        val btnForward = findViewById<Button>(R.id.btnForward)
        val Stat_by_point = findViewById<Button>(R.id.Stat_by_point)
        val overall_stats = findViewById<Button>(R.id.overall_stats)
        val btnOvlRes = findViewById<Button>(R.id.showOverallRes)

        btnForward.setOnClickListener {
            JDBCConnector.closeConnection()
            val Intent = Intent(this, ChooseMarchActivity::class.java)
            startActivity(Intent)
        }

        //TODO: W późniejszych wersjach aplikacji poniższe przyciski będą przenosić
        //TODO: użytkownika do ekranów ze statystykami, które będą pobierać statystyki
        //TODO: za pomocą odpowiednich kwerend.

        Stat_by_point.setOnClickListener {
//            Toast.makeText(
//                this,
//                "To będzie podgląd statystyk dla punktu!",
//                Toast.LENGTH_SHORT
//            ).show()
            val Intent = Intent(this, ChoosePointToViewStat::class.java)
            startActivity(Intent)
//            val intent = Intent(this, SelectPointToFollow::class.java)
        }

        overall_stats.setOnClickListener {
//            Toast.makeText(
//                this,
//                "To będzie podgląd ogólnych statystyk!",
//                Toast.LENGTH_SHORT
//            ).show()
            val Intent = Intent(this, MarchResults::class.java)
            startActivity(Intent)
        }


        // tutaj przeniesie nas do ekranu z wykresem kolowym
        btnOvlRes.setOnClickListener{
            val Intent = Intent(this, PieChartParWhoPassedAGivenPoint::class.java)
            startActivity(Intent)
        }

    }
}