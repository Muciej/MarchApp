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

        btnSign.setOnClickListener{
            if (username.text.isNullOrBlank() || password.text.isNullOrBlank())
            {
                Toast.makeText(this, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
            }
            else
            {
                var isCorrect = true
                if (username.text.toString().equals("admin") && password.text.toString().equals("admin"))
                {
                    Toast.makeText(this, "Zalogowano jako admin!", Toast.LENGTH_SHORT).show()
                }

                else if (username.text.toString().equals("organizator") && password.text.toString().equals("organizator"))
                {
                    Toast.makeText(this, "Zalogowano jako organizator!", Toast.LENGTH_SHORT).show()
                }

                else if (username.text.toString().equals("uczestnik") && password.text.toString().equals("uczestnik"))
                {
                    Toast.makeText(this, "Zalogowano jako uczestnik!", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(this, "Niepoprawne dane, spróbuj ponownie!", Toast.LENGTH_SHORT).show()
                    isCorrect = false
                }


                //Jak narazie przenosi spowrotem do Main Activity - czekam na resztę ekranów
                if (isCorrect)
                {
                    val Intent = Intent(this, MainActivity::class.java)
                    startActivity(Intent)
                }

            }
        }
    }

}