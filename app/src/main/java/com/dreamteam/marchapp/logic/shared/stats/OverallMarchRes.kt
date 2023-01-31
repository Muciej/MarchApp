package com.dreamteam.marchapp.logic.shared.stats

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.DBConnector
import com.dreamteam.marchapp.database.JDBCConnector
import java.time.LocalDateTime
import java.util.*

//TODO Ogarnąć jak liczyć czas zawodów
class OverallMarchRes : AppCompatActivity() {
    private var connector: DBConnector = JDBCConnector
    private var names = Vector<String>()
    private var secondNames = Vector<String>()
    private var times = Vector<String>()
    private var timesDifferences = Vector<LocalDateTime>()
    private var results = Vector<String>()

    private var dates = Vector<LocalDateTime>()
//    @RequiresApi(Build.VERSION_CODES.O)
//    private var startTime = LocalDateTime.parse("2023-01-30 08:00:00")


    //@RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overall_march_res)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val mListView = findViewById<ListView>(R.id.userlist)

        /**
         * To zapytanie sortuje nam czasy na punkcie "Meta", mamy więc gwarancję, że są to wyniki uczestników.
         */
        connector.prepareQuery("select u.imie, u.nazwisko, up.data, u.nr_startowy from uczestnicy u\n" +
                "inner join uczestnik_punkt up on u.nr_startowy = up.id_uczestnika\n" +
                "inner join punkty_kontrolne pk on up.id_punktu = pk.id\n" +
                "where pk.nazwa like 'Meta'\n" +
                "order by data")
        var answer : Vector<Vector<String>> = Vector<Vector<String>>()
        try {
            connector.executeQuery()
            answer = connector.getAnswer()
        } catch (e : Exception){
            showToast(applicationContext, "Spróbuj ponownie połączyć się z bazą!")
        }

        for(row in answer){
            names.add(row[0])
            secondNames.add(row[1])
            times.add(row[2])
        }

        /**
         * Zapisujemy daty w formacie LocalDataTime
         */
//        for (i in times){
//            dates.add(LocalDateTime.parse(i))
//        }
//
//        for (i in dates){
//            val hour8hoursBefore = i.minusHours(8)
//            timesDifferences.add(hour8hoursBefore)
//        }

        /**
         * To wypisujemy w liście
         */
        for (i in names.indices) {
            results.add((i+1).toString() + " " + names[i] + " " + secondNames[i] + " " + times[i])
        }

        val arrayAdapter: ArrayAdapter<*>

        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, results)
        mListView.adapter = arrayAdapter

        btnBack.setOnClickListener {
            val intent = Intent(this, MarchResults::class.java)
            startActivity(intent)
        }
    }
    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }
}