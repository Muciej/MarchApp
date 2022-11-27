package com.dreamteam.marchapp.logic.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dreamteam.marchapp.R

class UpdateParticipantData : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_participant_data)

        val username = findViewById<TextView>(R.id.username)
        val email = findViewById<TextView>(R.id.email)
        val phone = findViewById<TextView>(R.id.phone)
        val updateBtn = findViewById<Button>(R.id.updateBtn)
        val backBtn = findViewById<Button>(R.id.btnBack)

        backBtn.setOnClickListener {
            val Intent = Intent(this, AdministratorMain::class.java)
            startActivity(Intent)
        }

        updateBtn.setOnClickListener {
            if (username.text.isNullOrBlank() || email.text.isNullOrBlank() ||
                phone.text.isNullOrBlank()
            ) {
                Toast.makeText(this, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
            } else {
                var isCorrect = true

                //Tu lecą zapytania do bazy
                //1. Zapytanie o to czy dana nazwa użytkownika istnieje w bazie,
                //2. Ewentualne zapytania o format email i numeru telefonu
                //(zależy od tego jak będziemy to sprawdzać).

                if (username.text.toString() != "login") {
                    Toast.makeText(
                        this,
                        "Taki użytkownik nie istnieje!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                } else if (email.text.toString().equals("123")) {
                    Toast.makeText(
                        this,
                        "Nieprawidłowy format email!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                } else if (phone.text.toString().equals("abc")) {
                    Toast.makeText(
                        this,
                        "Nieprawidłowy format numeru!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }

                //Po aktualizacji wracam do ekranu głównego administratora.
                if (isCorrect) {
                    Toast.makeText(
                        this,
                        "Aktualizacja przebiegła poprawnie!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, AdministratorMain::class.java)
                    startActivity(intent)

                    //TODO:Tutaj będzie leciało zapytanie do bazy, które zaktualizuje nam uczestnika,
                    //TODO: podanych danych, czyli username, email i phone
                }

            }
        }
    }
}