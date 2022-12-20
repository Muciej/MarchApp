package com.dreamteam.marchapp.logic.shared.stats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dreamteam.marchapp.R
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

// w tej klasie wykres: na osi pionowej czas uczestników na danym punkcie, na osi poziomej uczestnicy z danej kategorii,
// którą wybraliśmy wcześniej
class AvgTimeOfParOnPoint : AppCompatActivity() {
    lateinit var lineGraphView: GraphView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avg_time_of_par_on_point)

        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, ChoosePointToViewStat::class.java)
            startActivity(intent)
        }

        // w poniższym wierszu inicjujemy
        // nasza zmienna z ich identyfikatorami.
        lineGraphView = findViewById(R.id.idGraphView)

        // w poniższej linii dodajemy dane do naszego widoku wykresu.
        //TODO dodac dane z bazy: wspolrzedna x- uczestnicy, wspolrzadna y- ich czas na punkcie
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                // on below line we are adding
                // each point on our x and y axis.
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 3.0),
                DataPoint(2.0, 4.0),
                DataPoint(3.0, 9.0),
                DataPoint(4.0, 6.0),
                DataPoint(5.0, 3.0),
                DataPoint(6.0, 6.0),
                DataPoint(7.0, 1.0),
                DataPoint(8.0, 2.0)
            )
        )

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
