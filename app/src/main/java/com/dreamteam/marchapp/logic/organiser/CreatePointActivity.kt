package com.dreamteam.marchapp.logic.organiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.DataViewModel
import com.dreamteam.marchapp.database.dataclasses.CheckPoint

class CreatePointActivity : AppCompatActivity() {
    private lateinit var dataViewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_point)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]

        val name = findViewById<TextView>(R.id.name)
        val online = findViewById<TextView>(R.id.online)
        val km = findViewById<TextView>(R.id.km)
        val coordinates = findViewById<TextView>(R.id.coordinates)
        val addBtn = findViewById<Button>(R.id.addPointButton)
        val backBtn = findViewById<Button>(R.id.btnBack)

        backBtn.setOnClickListener {
            val intent = Intent(this, OrganisatorModifyEventMenu::class.java)
            startActivity(intent)
        }

        addBtn.setOnClickListener {
            if (name.text.isNullOrBlank() || online.text.isNullOrBlank() ||
                coordinates.text.isNullOrBlank() || km.text.isNullOrBlank()
            ) {
                Toast.makeText(this, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
            } else {
                var isCorrect = true

                if (dataViewModel.existsCheckPointByName(name.toString()) == true) {
                    Toast.makeText(
                        this@CreatePointActivity,
                        "Punkt o podanej nazwie już istnieje w bazie!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                } else if (dataViewModel.existsCheckPointByKm(Integer.valueOf(km.toString())) == true) {
                    Toast.makeText(
                        this@CreatePointActivity,
                        "Punkt na danym kilometrze już istnieje w bazie!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                } else if (online.toString().toInt() != 0 && online.toString().toInt() != 1) {
                    Toast.makeText(
                        this@CreatePointActivity,
                        "Online musi być 0 lub 1",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                } else if (dataViewModel.existsCheckPointByCords(coordinates.toString()) == true) {
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

    private fun addPoint() {
        val name = findViewById<TextView>(R.id.name)
        val online = findViewById<TextView>(R.id.online)
        val km = findViewById<TextView>(R.id.km)
        val coordinates = findViewById<TextView>(R.id.coordinates)

        try {
            val isOnline = online.text.toString() == "1"
            //TODO: When adding to the database, we should not have to specify the ID!
            dataViewModel.addNewCheckPoint(
                CheckPoint(
                    1,
                    isOnline,
                    name.text.toString(),
                    Integer.valueOf(km.text.toString()),
                    coordinates.text.toString()
                )
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}