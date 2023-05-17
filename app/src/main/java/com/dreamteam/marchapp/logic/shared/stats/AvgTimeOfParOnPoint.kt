package com.dreamteam.marchapp.logic.shared.stats

import android.content.Context
import android.content.Intent
import android.os.Build

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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
//todo ogarnąć jakoś jak mierzyć czas i wstawiać go do wykresu
class AvgTimeOfParOnPoint : AppCompatActivity() {
    lateinit var lineGraphView: GraphView
    private var connector: DBConnector = JDBCConnector
    private var participants = Vector<String>()
    private var timesOnPoint = Vector<String>()
    private var points = Vector<String>()
    private var timesDifferences = Vector<LocalDateTime>()

    private var dates = Vector<LocalDateTime>()
//    @RequiresApi(Build.VERSION_CODES.O)
//    private var startTime = LocalDateTime.parse("2023-01-30 08:00:00")

    private val choosePointToViewStat = ChoosePointToViewStat() // obiekt klasy ChoosepointToViewStat
    private val spinnerVal = choosePointToViewStat.spinnerValue


    //@RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avg_time_of_par_on_point)

        connector.startConnection()

        /**
         * Numery startowe uczestników z bazy danych - oś OX
         * Czas uczestników na punkcie - oś OY
         */

        connector.prepareQuery("select * from czas_uczestnicy_punkt;")
        var answer : Vector<Vector<String>> = Vector<Vector<String>>()
        try {
            connector.executeQuery()
            answer = connector.getAnswer()
        } catch (e : Exception){
            showToast(applicationContext,"Nie udało się połączyć z bazą!")
        }

        for(row in answer){
            points.add(row[3]) // puntky trasy
            participants.add(row[9]) //numer startowy uczestnika
            timesOnPoint.add(row[8]) // kolumna z czasami uczestników
            println(row[3] + " " + row[9] + " " + row[8])
        }

        /**
         * Zapisujemy daty w formacie LocalDataTime
         * konwertujemy Stringa na LocalDataTime
         */
//        for (i in timesOnPoint){
//            dates.add(LocalDateTime.parse(i))
//        }

        /**
         * Obliczamy czas od jaki upłynął od startu
         */
//        for (i in dates){
//            val hour8hoursBefore = i.minusHours(8)
//            timesDifferences.add(hour8hoursBefore)
//        }


        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            connector.closeConnection()
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

        if (points.size == participants.size && participants.size == timesOnPoint.size) {
            for (i in participants.indices) {
                println("spinner: $spinnerVal")
                println(points[i])
                //if (points[i].equals(spinnerVal)) { //TODO poprawic porownywanie stringow zeby robic wykres dla jednego punktu
                    dataPointList.add(
                        DataPoint(
                            participants[i].toDouble(),
                            participants[i].toDouble()
                        )
                    )
                //}
            }
        }

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
