package com.dreamteam.marchapp.logic.organiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.JDBCConnector

class CreateAdminInMarchAccount : AppCompatActivity() {

    val connector = JDBCConnector

    /**
     * Invoked when data is check for valid values and
     * we want to send query to database
     */
    fun registerAdmin(){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_admin_in_march_account)

        val username = findViewById<TextView>(R.id.username)
        val password = findViewById<TextView>(R.id.password)
        val repPassword = findViewById<TextView>(R.id.repeatPassword)
        val email = findViewById<TextView>(R.id.email)
        val phoneNr = findViewById<TextView>(R.id.number)
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val backBtn = findViewById<Button>(R.id.btnBack)

        backBtn.setOnClickListener{
            val Intent = Intent(this, OrganisatorMain::class.java)
            startActivity(Intent)
        }

        registerBtn.setOnClickListener{
            if (username.text.isNullOrBlank() || password.text.isNullOrBlank() ||
                repPassword.text.isNullOrBlank() || email.text.isNullOrBlank() ||
                phoneNr.text.isNullOrBlank()
            )
            {
                Toast.makeText(this, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
            }
            else
            {
                var isCorrect = true

                //TODO:Tu lecą zapytania do bazy
                //TODO:1. Zapytanie o to czy dana nazwa użytkownika jest zajęta,
                //TODO:2. Ewentualne zapytania o format email i numeru telefonu
                //TODO:(zależy od tego jak będziemy to sprawdzać).


                if (username.text.toString().equals("login")) {
                    Toast.makeText(
                        this,
                        "Użytkownik o tej nazwie już istnieje!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }
                else if (password.text.toString() != repPassword.text.toString()) {
                    Toast.makeText(
                        this,
                        "Wprowadzone hasła muszą być identyczne!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }
                else if (email.text.toString().equals("123")) {
                    Toast.makeText(
                        this,
                        "Nieprawidłowy format email!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }
                else if (phoneNr.text.toString().equals("abc")) {
                    Toast.makeText(
                        this,
                        "Nieprawidłowy format numeru!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }

                //Po rejestracji wracam do ekranu głównego administratora.
                if (isCorrect)
                {
                    Toast.makeText(
                        this,
                        "Rejestracja przebiegła poprawnie!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val Intent = Intent(this, OrganisatorMain::class.java)
                    startActivity(Intent)

                    //TODO: Podczas rejestracji hashujemy haslo:
//                    val hashedPass = Hasher.hash(password.text.toString())

                    //TODO:Tutaj będzie leciało zapytanie do bazy, które stworzy nam administratora,
                    //TODO:z podanych danych, czyli username, password, email i phoneNr
                }
            }
        }
    }
}