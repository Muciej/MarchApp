package com.dreamteam.marchapp.logic.shared.stats

import android.content.Context
import android.content.Intent
import android.os.Build

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi

import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.DBConnector
import com.dreamteam.marchapp.database.JDBCConnector
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.time.LocalDateTime

import java.util.*


/**
 * w tej klasie ciągły wykres: na osi pionowej czas uczestników na danym punkcie,
 * na osi poziomej uczestnicy według ich numerów startowych
 */
//TODO zrobic pobieranie danych z bazy i wyswietlanie ich na wykresie
class AvgTimeOfParOnPoint : AppCompatActivity() {
    private lateinit var lineGraphView: GraphView
    private lateinit var chosenPoint: TextView
    private var connector: DBConnector = JDBCConnector
    private var participants = Vector<String>()
    private var timesOnPoint = Vector<String>()
    private var points = Vector<String>()
    private var timesDifferences = Vector<LocalDateTime>()

    private var dates = Vector<LocalDateTime>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avg_time_of_par_on_point)
        chosenPoint = findViewById<TextView>(R.id.textView2)
        val spinnerVal = intent.getStringExtra("selectedPoint")

        chosenPoint.text = spinnerVal

        /**
         * Numery startowe uczestników z bazy danych - oś OX
         * Czas uczestników na punkcie - oś OY
         */

//        connector.prepareQuery("select * from czas_uczestnicy_punkt;")



        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, ChoosePointToViewStat::class.java)
            startActivity(intent)
        }

        // w poniższym wierszu inicjujemy
        // nasza zmienna z ich identyfikatorami.
        lineGraphView = findViewById(R.id.idGraphView)

        /**
         * w poniższej linii dodajemy dane do naszego widoku wykresu.
         * wspolrzedna x- uczestnicy, wspolrzadna y- ich czas na punkcie
         */

        val dataPointList = Vector<DataPoint>()

        dataPointList.add(DataPoint(2.0, 40.0))
        dataPointList.add(DataPoint(4.0, 80.0))
        dataPointList.add(DataPoint(6.0, 35.0))
        dataPointList.add(DataPoint(8.0, 60.0))
        dataPointList.add(DataPoint(10.0, 68.0))



        val series: LineGraphSeries<DataPoint> = LineGraphSeries(dataPointList.toTypedArray())

        // w poniższej linii dodajemy animację
        lineGraphView.animate()

        // w poniższym wierszu ustawiamy możliwość przewijania
        // dla widoku wykresu
        lineGraphView.viewport.isScrollable = true

        // w poniższym wierszu ustawiamy skalowalność.
        lineGraphView.viewport.isScalable = true

        // w poniższym wierszu ustawiamy skalowalny y
        lineGraphView.viewport.setScalableY(true)

        // w poniższym wierszu ustawiamy y, ktory da sie scrollowac
        lineGraphView.viewport.setScrollableY(true)

        series.color = R.color.purple_500

        lineGraphView.addSeries(series)
    }
    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }

}
