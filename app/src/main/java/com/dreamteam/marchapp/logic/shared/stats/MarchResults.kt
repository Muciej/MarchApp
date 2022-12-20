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

// w tej klasie ogolne wyniki marszu dla kazdej z kategorii
class MarchResults : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    //TODO tu będą nazwy punktów i kategorii z bazy danych
    var categories = arrayOf("1", "2", "3", "4", "5", "6")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marchresults)

        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        val btnChoose = findViewById<Button>(R.id.chooseBtn)
        val btnBack = findViewById<Button>(R.id.btnBack)

        val aa1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        // tutaj w zaleznosci co wybraliśmy switchuje odpowiednia kategorie
        with(spinner2)
        {
            adapter = aa1
            setSelection(0, false)
            onItemSelectedListener = this@MarchResults
            prompt = "Select point"
            gravity = Gravity.CENTER

        }

        //przycisk powrotu do ekranu statystyk

        btnBack.setOnClickListener {
            val Intent = Intent(this, ViewSt::class.java)
            startActivity(Intent)
        }

        // bedzie nas przenosilo do listy uczestnikow, ktorzy przeszli dany punkt
        btnChoose.setOnClickListener {
            val Intent = Intent(this, OverallMarchRes::class.java)
            startActivity(Intent)
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        showToast(message = "Wybrano kategorię: ${categories[p2]}")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        showToast(message = "Nie wybrano nic")
    }
    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }
}