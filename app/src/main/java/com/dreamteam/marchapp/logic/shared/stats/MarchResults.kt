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
class MarchResults : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marchresults)

        val btnChoose = findViewById<Button>(R.id.chooseBtn)
        val btnBack = findViewById<Button>(R.id.btnBack)


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

    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }
}