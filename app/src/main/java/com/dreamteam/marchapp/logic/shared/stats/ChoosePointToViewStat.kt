package com.dreamteam.marchapp.logic.shared.stats

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.DBConnector
import com.dreamteam.marchapp.database.JDBCConnector
import com.dreamteam.marchapp.logic.shared.ViewSt
import java.util.*

class ChoosePointToViewStat : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var connector: DBConnector = JDBCConnector
    var points = Vector<String>()
    var spinnerValue : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_point_to_view_stat)

        /**
         * Nazwy punktów z bazy danych
         */

        connector.startConnection()
        connector.prepareQuery("SELECT * FROM punkty_kontrolne;")
        connector.executeQuery()
        points = connector.getCol(4)


        val spinner= findViewById<Spinner>(R.id.spinner2)


        val btnChoose = findViewById<Button>(R.id.chooseBtn)
        val btnBack = findViewById<Button>(R.id.btnBack)

        val aa1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, points)
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        // tutaj w zaleznosci co wybraliśmy switchuje odpowiedni punkt, na którym bedzimy przegladac statystyke
        with(spinner)
        {
            adapter = aa1
            setSelection(0, false)
            onItemSelectedListener = this@ChoosePointToViewStat
            prompt = "Select point"
            gravity = Gravity.CENTER

        }

        spinnerValue = spinner.selectedItem.toString()

        //przycisk powrotu do ekranu statystyk

        btnBack.setOnClickListener{
            val intent = Intent(this, ViewSt::class.java)
            startActivity(intent)
        }


        // bedzie nas przenosilo do wykresu ze statystykami
        btnChoose.setOnClickListener{
            connector.closeConnection()
            val intent = Intent(this, AvgTimeOfParOnPoint::class.java)
            startActivity(intent)
        }

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        showToast(message = "Wybrano punkt:${p2}")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        showToast(message = "Nie wybrano nic")
    }
    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }
}