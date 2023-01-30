package com.dreamteam.marchapp.logic.shared.stats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.DBConnector
import com.dreamteam.marchapp.database.JDBCConnector
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.util.*
import kotlin.collections.HashMap

/**
 * w tej klasie ciągły wykres: na osi pionowej czas uczestników na danym punkcie,
 * na osi poziomej uczestnicy według ich numerów startowych
 */
class AvgTimeOfParOnPoint : AppCompatActivity() {
    lateinit var lineGraphView: GraphView
    private var connector: DBConnector = JDBCConnector
    private var participants = Vector<String>()
    private var timesOnPoint = Vector<String>()
    private var points = Vector<String>()


    private val choosePointToViewStat = ChoosePointToViewStat() // obiekt klasy ChoosepointToViewStat
    private val spinnerValue = choosePointToViewStat.spinnerValue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avg_time_of_par_on_point)

        connector.startConnection()

        /**
         * Numery startowe uczestników z bazy danych - oś OX
         * Czas uczestników na punkcie - oś OY
         */
        // dostajemy tabele w której 1-kolumna to nazwy marszów, 2 to nr_startowy uczestnika, 3 to czas na danym punkcie
//        connector.prepareQuery("select *\n" +
//                "from ev_test_event.punkty_kontrolne p\n" +
//                "inner join ev_test_event.uczestnik_punkt up on p.id = up.id_punktu\n" +
//                "inner join ev_test_event.uczestnicy u on up.id_uczestnika = u.nr_startowy\n" +
//                "order by u.nr_startowy;\n")

        connector.prepareQuery("select * from czas_uczestnicy_punkt;")
        var answer : Vector<Vector<String>> = Vector<Vector<String>>()
        try {
            connector.executeQuery()
//            points = connector.getCol(4) // puntky trasy
//            participants = connector.getCol(10) //numer startowy uczestnika
//            timesOnPoint = connector.getCol(9) // kolumna z czasami uczestników

            answer = connector.getAnswer()
        } catch (e : Exception){
            //todo toast informujący o błędzie
        }

        for(row in answer){
            points.add(row[3])
            participants.add(row[9])
            timesOnPoint.add(row[8])
            println(row[3] + " " + row[9] + " " + row[8])
        }

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
        val i = dataPointList.iterator()
        if(points.size == participants.size && participants.size == timesOnPoint.size) {

            i.forEach { _ ->
                if (points[i.toString().toInt()] == spinnerValue) {
                    dataPointList.add(
                        DataPoint(
                            participants[i.toString().toInt()].toDouble(),
                            timesOnPoint[i.toString().toInt()].toDouble()
                        )
                    )
                }
            }

        }

//        for (i  in participants){
//           dataPointList.add(DataPoint(i.toDouble(),i.toDouble()))
//        }

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

}
