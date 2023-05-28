package com.dreamteam.marchapp.logic.organiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.JDBCConnector

class CreatePointActivity : AppCompatActivity() {

    var connector = JDBCConnector

    fun addPoint(){
        val nazwa = findViewById<TextView>(R.id.name)
        val online = findViewById<TextView>(R.id.online)
        val km = findViewById<TextView>(R.id.km)
        val wsp = findViewById<TextView>(R.id.coordinates)
        val addBtn = findViewById<Button>(R.id.addPointButton)
        val backBtn = findViewById<Button>(R.id.btnBack)

        try {
            connector.prepareQuery("insert into punkty_kontrolne (online, nazwa, kilometr, współrzędne_geograficzne)" +
                    " value (?, ?, ?, ?);")
            connector.setStrVar(nazwa.text.toString(), 2)
            connector.setStrVar(online.text.toString(), 1)
            connector.setStrVar(km.text.toString(), 3)
            connector.setStrVar(wsp.text.toString(), 4)

            connector.executeQuery()
        } catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun checkIfPresentInDB(obj : String, field : String) : Boolean {
        connector.prepareQuery("SELECT count(nazwa) FROM punkty_kontrolne where $field = '$obj'")
        connector.executeQuery()
        if (connector.getColInts(1)[0] == 0){
            return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_point)

        val nazwa = findViewById<TextView>(R.id.name)
        val online = findViewById<TextView>(R.id.online)
        val km = findViewById<TextView>(R.id.km)
        val wsp = findViewById<TextView>(R.id.coordinates)
        val addBtn = findViewById<Button>(R.id.addPointButton)
        val backBtn = findViewById<Button>(R.id.btnBack)


        backBtn.setOnClickListener {
            val Intent = Intent(this, OrganisatorModifyEventMenu::class.java)
            startActivity(Intent)
        }

        addBtn.setOnClickListener {
            if (nazwa.text.isNullOrBlank() || online.text.isNullOrBlank() ||
                wsp.text.isNullOrBlank() || km.text.isNullOrBlank()
            ) {
                Toast.makeText(this, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
            } else {
                var isCorrect = true

                if (checkIfPresentInDB(nazwa.toString(), "nazwa" ))
                {
                    Toast.makeText(
                        this@CreatePointActivity,
                        "Punkt o podanej nazwie już istnieje w bazie!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }

                else if (checkIfPresentInDB(km.toString(), "kilometr" ))
                {
                    Toast.makeText(
                        this@CreatePointActivity,
                        "Punkt na danym kilometrze już istnieje w bazie!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }

//                else if (kilometr.toString().toInt() < 0)
//                {
//                    Toast.makeText(
//                        this@CreatePointActivity,
//                        "Kilometr nie może być ujemny",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    isCorrect = false
//                }

                else if (online.toString().toInt() != 0 && online.toString().toInt() != 1)
                {
                    Toast.makeText(
                        this@CreatePointActivity,
                        "Online musi być 0 lub 1",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }

                else if (checkIfPresentInDB(wsp.toString(), "współrzędne_geograficzne" ))
                {
                    Toast.makeText(
                        this@CreatePointActivity,
                        "Punkt o podanych współrzędnych już istnieje w bazie!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }

                if (isCorrect) {
                    addPoint()
                    Toast.makeText(
                        this,
                        "Dodanie punktu przebiegło poprawnie!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, OrganisatorModifyEventMenu::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}