package com.dreamteam.marchapp.logic.shared.stats

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.logic.shared.ViewSt

class ParWhoPassedAGivenPoint : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    //TODO tu będą nazwy punktów i kategorii z bazy danych
    var points = arrayOf( "1", "2", "3", "4", "5", "6")
    var categories = arrayOf( "1", "2", "3", "4", "5", "6")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_par_who_passed_agiven_point)

        val spinner2= findViewById<Spinner>(R.id.spinner2)
        val spinner3= findViewById<Spinner>(R.id.spinner3)
        val btnChoose = findViewById<Button>(R.id.chooseBtn)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnOvlRes = findViewById<Button>(R.id.showOverallRes)

        val aa1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, points)
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val aa2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // tutaj w zaleznosci co wybraliśmy switchuje odpowiedni punkt, na którym bedzimy przegladac statystyke
        with(spinner2)
        {
            adapter = aa1
            setSelection(0, false)
            onItemSelectedListener = this@ParWhoPassedAGivenPoint
            prompt = "Select point"
            gravity = Gravity.CENTER

        }

        // tutaj w zaleznosci co wybraliśmy switchuje odpowiednia kategorie z bazy
        with(spinner3)
        {
            adapter = aa2
            setSelection(0, false)
            onItemSelectedListener = this@ParWhoPassedAGivenPoint
            prompt = "Select category"
            gravity = Gravity.CENTER

        }

        //przycisk powrotu do ekranu statystyk

        btnBack.setOnClickListener{
            val Intent = Intent(this, ViewSt::class.java)
            startActivity(Intent)
        }


        // bedzie nas przenosilo do listy uczestnikow, ktorzy przeszli dany punkt
        btnChoose.setOnClickListener{
            val Intent = Intent(this, ListOfParWhoPassedAGivenPoint::class.java)
            startActivity(Intent)
        }

        // tutaj przeniesie nas do ekranu z wykresem kolowym
        btnOvlRes.setOnClickListener{
            val Intent = Intent(this, PieChartParWhoPassedAGivenPoint::class.java)
            startActivity(Intent)
        }

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        showToast(message = "Wybrano punkt:${p2} i kategorię: ${categories[p2]}")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        showToast(message = "Nie wybrano nic.")
    }
    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }
}