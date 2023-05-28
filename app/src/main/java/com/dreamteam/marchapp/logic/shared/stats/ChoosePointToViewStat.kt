package com.dreamteam.marchapp.logic.shared.stats

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.DataViewModel
import com.dreamteam.marchapp.logic.shared.ViewSt
import java.util.*
import androidx.lifecycle.Observer
import com.dreamteam.marchapp.database.dataclasses.CheckPoint

/**
 * Klasa korzysta z View Model
 */
class ChoosePointToViewStat : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    private lateinit var dataViewModel: DataViewModel
    var spinnerValue : String = ""
    var points: List<CheckPoint>? = null
    var pointNames = Vector<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_point_to_view_stat)
        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]

        dataViewModel.checkPoints.observe(this, Observer {
                newEvents -> pointsUpdated(newEvents)
        })


        val btnChoose = findViewById<Button>(R.id.chooseBtn)
        val btnBack = findViewById<Button>(R.id.btnBack)

        //przycisk powrotu do ekranu statystyk OKEJ

        btnBack.setOnClickListener{
            val intent = Intent(this, ViewSt::class.java)
            startActivity(intent)
        }


        // bedzie nas przenosilo do wykresu ze statystykami
        btnChoose.setOnClickListener{
            val intent = Intent(this, AvgTimeOfParOnPoint::class.java)
            intent.putExtra("selectedPoint", spinnerValue)
            startActivity(intent)
        }

    }

    fun pointsUpdated(points: List<CheckPoint>?){
        this.points = points

        if (points != null) {
            for(point in points){
                pointNames.add(point.name)
            }
        }
        updateSpinner()
    }

    private fun updateSpinner(){
        val myspinner= findViewById<Spinner>(R.id.spinner2)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, pointNames)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // tutaj w zaleznosci co wybraliśmy switchuje odpowiednią bazę z konkretnego marszu
        with(myspinner)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@ChoosePointToViewStat
            prompt = "Select point"
            gravity = Gravity.CENTER
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selectedPoint = pointNames[p2]
        spinnerValue = selectedPoint // Update the spinnerValue variable with the selected value
        showToast(message = "Wybrano punkt: $selectedPoint")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        showToast(message = "Nie wybrano nic")
    }
    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }
}