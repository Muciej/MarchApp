package com.dreamteam.marchapp.logic.shared

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dreamteam.marchapp.R

//TODO Zrobić ekran strtowy, z którego idzieĶy do ekranu zapisanego w tej klasie
class ChooseMarchActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    //TODO tu będą nazwy marszów z bazy danych
    var marches = arrayOf( "1", "2", "3", "4", "5", "6")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_march)

        val myspinner= findViewById<Spinner>(R.id.spinner2)
        val btnChoose = findViewById<Button>(R.id.chooseBtn)

        var aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, marches)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // tutaj w zaleznosci co wybraliśmy switchuje odpowiednią bazę z konkretnego marszu
        with(myspinner)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@ChooseMarchActivity
            prompt = "Select march"
            gravity = Gravity.CENTER

        }


        // przycisk zatwierdzenia wybranego marszu
        btnChoose.setOnClickListener{
            val Intent = Intent(this, LoginActivity::class.java)
            startActivity(Intent)
        }

    }

    //TODO tutaj switch bazy danych z odpowiednim marszem
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        showToast(message = "spinner position:${p2} and march: ${marches[p2]}")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        showToast(message = "Nothing selected")
    }
    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }


}