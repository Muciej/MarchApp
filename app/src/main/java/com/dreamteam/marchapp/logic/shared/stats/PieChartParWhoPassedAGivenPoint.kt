package com.dreamteam.marchapp.logic.shared.stats

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.logic.shared.ViewSt
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF

// TODO w xml dodac odpowiednie kategorie uczestnikow
class PieChartParWhoPassedAGivenPoint : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart_par_who_passed_agiven_point)

        val pieChart = findViewById<PieChart>(R.id.pieChart)
        val backBtn = findViewById<Button>(R.id.btnBack)

        // w poniższej linii ustawiamy wartość procentową użytkownika,
        // opis ustawienia jako włączony i offset
        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(false)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // w poniższej linii ustawiamy szybkosc animacji dla naszego wykresu kołowego
        pieChart.setDragDecelerationFrictionCoef(0.95f)

        // w poniższej linii ustawiamy otwór
        // i kolor otworu dla wykresu kołowego
        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.WHITE)

        // ustaw kolor przezroczystego kola w srodku
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // w poniższej linii ustawiamy promień otworu
        pieChart.setHoleRadius(58f)
        pieChart.setTransparentCircleRadius(61f)

        // w poniższym wierszu ustawiamy środkowy tekst
        pieChart.setDrawCenterText(true)

        pieChart.setRotationAngle(0f)

        // włącz obracanie wykresu kołowego dotykiem
        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)

        // w poniższej linii ustawiamy animację dla naszego wykresu kołowego
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // w poniższym wierszu włączamy naszą legendę dla wykresu
        pieChart.legend.isEnabled = true
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        // TODO tutaj dodajemy kategorie z bazy i ich stosunek procentowy
        // w poniższym wierszu tworzymy array liste i
        // dodajemy do niej dane do wyświetlenia na wykresie
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(70f))
        entries.add(PieEntry(20f))
        entries.add(PieEntry(10f))

        val dataSet = PieDataSet(entries, "Kategorie")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // dodaj kolory do listy
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.purple_200))
        colors.add(resources.getColor(R.color.yellow))
        colors.add(resources.getColor(R.color.red))

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.setData(data)

        // cofnij wszystkie podświetlenia
        pieChart.highlightValues(null)

        // ladowanie wykresu
        pieChart.invalidate()

        backBtn.setOnClickListener{
            val Intent = Intent(this, ViewSt::class.java)
            startActivity(Intent)
        }
    }
    }
