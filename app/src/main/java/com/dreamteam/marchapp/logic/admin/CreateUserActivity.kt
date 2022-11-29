package com.dreamteam.marchapp.logic.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.logic.validation.EmailValidator
import com.dreamteam.marchapp.logic.validation.PasswordValidator
import com.dreamteam.marchapp.logic.validation.PhoneValidator
import com.dreamteam.marchapp.logic.validation.UsernameValidator

class CreateUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        val username = findViewById<TextView>(R.id.username)
        val password = findViewById<TextView>(R.id.password)
        val repPassword = findViewById<TextView>(R.id.repeatPassword)
        val email = findViewById<TextView>(R.id.name)
        val phoneNr = findViewById<TextView>(R.id.lastname)
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val backBtn = findViewById<Button>(R.id.btnBack)

        backBtn.setOnClickListener {
            val Intent = Intent(this, AdministratorMain::class.java)
            startActivity(Intent)
        }

        registerBtn.setOnClickListener {
            if (username.text.isNullOrBlank() || password.text.isNullOrBlank() ||
                repPassword.text.isNullOrBlank() || email.text.isNullOrBlank() ||
                phoneNr.text.isNullOrBlank()
            ) {
                Toast.makeText(this, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
            } else {
                var isCorrect = true

                //Tu lecą zapytania do bazy
                //1. Zapytanie o to czy dana nazwa użytkownika jest zajęta,
                //(zależy od tego jak będziemy to sprawdzać).

                if (username.text.toString().equals("login")) {
                    Toast.makeText(
                        this,
                        "Użytkownik o tej nazwie już istnieje!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }
                else if(!UsernameValidator.validate(username.text.toString())) {
                    Toast.makeText(
                        this,
                        "Nieprawidlowy format nazwy uzytkownika (5-15 znakow, tylko litery i cyfry)",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false

                } else if (!PasswordValidator.validate(password.text.toString())) {
                    Toast.makeText(
                        this,
                        "Nienprawidlowa dlugosc hasla (8-64 znaki)",
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
                } else if (!EmailValidator.validate(email.text.toString())) {
                    Toast.makeText(
                        this,
                        "Nieprawidłowy format email!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                } else if (!PhoneValidator.validate(phoneNr.text.toString())) {
                    Toast.makeText(
                        this,
                        "Nieprawidłowy format numeru!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }

                //Po rejestracji wracam do ekranu głównego administratora.
                if (isCorrect) {
                    Toast.makeText(
                        this,
                        "Rejestracja przebiegła poprawnie!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, AdministratorMain::class.java)
                    startActivity(intent)

                    //Tutaj będzie leciało zapytanie do bazy, które stworzy nam administratora,
                    //z podanych danych, czyli username, password, email i phoneNr
                    //tutaj hashujemy tez haslo: val hashedPassword: String =
                    //                    BCrypt.withDefaults().hashToString(12,password.text.toString().toCharArray())
                }
            }
        }
    }
}