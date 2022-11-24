package com.dreamteam.marchapp.logic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dreamteam.marchapp.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<TextView>(R.id.username)
        val password = findViewById<TextView>(R.id.password)
        val btnSign = findViewById<Button>(R.id.signbtn)
        val backBtn = findViewById<Button>(R.id.btnBack)

        backBtn.setOnClickListener{
            val Intent = Intent(this, MainActivity::class.java)
            startActivity(Intent)
        }

        btnSign.setOnClickListener{
            if (username.text.isNullOrBlank() || password.text.isNullOrBlank())
            {
                Toast.makeText(this, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
            }
            else
            {
                var isCorrect = true
                var isAdministrator = false
                var isWolontariusz = false

                //tu leci zapyutanie do bazy
                //W zależności od roli jaką zwróciło zapytanie, przełączamy na inny ekran
                //Gdy nie zwróciło nic - niepoprawne dane
                //przykładowe zapytanie
                //SELECT rola_id FROM konta WHERE login = 'admin' AND hasło = 'admin'

                if (username.text.toString().equals("admin") && password.text.toString().equals("admin"))
                {
                    isAdministrator = true
                    val Intent = Intent(this, AdministratorMain::class.java)
                    startActivity(Intent)
                }

                else if (username.text.toString().equals("organizator") && password.text.toString().equals("organizator"))
                {
                    Toast.makeText(this, "Zalogowano jako organizator!", Toast.LENGTH_SHORT).show()
                }

                else if (username.text.toString().equals("uczestnik") && password.text.toString().equals("uczestnik"))
                {
                    Toast.makeText(this, "Zalogowano jako uczestnik!", Toast.LENGTH_SHORT).show()
                }
                else if (username.text.toString().equals("wolontariusz") && password.text.toString().equals("wolontariusz"))
                {
                    isWolontariusz = true
                    val Intent = Intent(this, VolunteerMain::class.java)
                    startActivity(Intent)
                }
                else
                {
                    Toast.makeText(this, "Niepoprawne dane, spróbuj ponownie!", Toast.LENGTH_SHORT).show()
                    isCorrect = false
                }


                //Jak narazie przenosi spowrotem do Main Activity - czekam na resztę ekranów
                if (isCorrect && !isAdministrator && !isWolontariusz)
                {
                    val Intent = Intent(this, MainActivity::class.java)
                    startActivity(Intent)
                }

            }
        }
    }

}