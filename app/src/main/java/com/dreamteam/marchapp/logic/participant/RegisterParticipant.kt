package com.dreamteam.marchapp.logic.participant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.JDBCConnector
import com.dreamteam.marchapp.logic.shared.LoginActivity
import com.dreamteam.marchapp.logic.validation.*

class RegisterParticipant : AppCompatActivity(){
    var connector = JDBCConnector
    /**
     * Invoked when all data is checked and we need a query to create new participant account
     */
    fun registerUser() : Boolean{

        val username = findViewById<TextView>(R.id.username)
        val phonenr = findViewById<TextView>(R.id.phonenr)
        val name = findViewById<TextView>(R.id.name)
        val lastname = findViewById<TextView>(R.id.lastname)

        connector.startConnection()
        connector.prepareQuery("insert into uczestnicy_do_akceptacji (imie, nazwisko, pseudonim, nr_telefonu) value (?,?,?,?);")
        connector.setStrVar(name.text.toString(), 1)
        connector.setStrVar(lastname.text.toString(), 2)
        connector.setStrVar(username.text.toString(), 3)
        connector.setStrVar(phonenr.text.toString(), 4)

        try{
            connector.executeQuery()
        } catch (e : Exception){
            Toast.makeText(this, "Nie udało się zarejestrować, spróbuj ponownie!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_participant)

        val username = findViewById<TextView>(R.id.username)
        val phonenr = findViewById<TextView>(R.id.phonenr)
        val name = findViewById<TextView>(R.id.name)
        val lastname = findViewById<TextView>(R.id.lastname)
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val backBtn = findViewById<Button>(R.id.btnBack)

        backBtn.setOnClickListener {
            val Intent = Intent(this, LoginActivity::class.java)
            startActivity(Intent)
        }

        registerBtn.setOnClickListener {
            if (username.text.isNullOrBlank() || phonenr.text.isNullOrBlank() || name.text.isNullOrBlank() ||
                lastname.text.isNullOrBlank()
            ) {
                Toast.makeText(this, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
            } else {
                var isCorrect = true
                var loginTaken = true

                /**
                 * TODO: Sprawdzić czy poniższe są okej.
                 */
                connector.startConnection()
                connector.prepareQuery("select * from uczestnicy_do_akceptacji where pseudonim = ?")
                connector.setStrVar(username.text.toString(), 1)
                connector.executeQuery()
                try {
                    connector.getAnswer()
                } catch(e: Exception){
                    loginTaken = false
                }

                if (loginTaken) {
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
                        "Nieprawidlowy format nazwy uzytkownika (5-15 znakow, tylko litery i cyfry)!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false

                } else if (!PhoneValidator.validate(phonenr.text.toString())) {
                    Toast.makeText(
                        this,
                        "Nienprawidlowy format numeru telefonu!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }
                 else if (!NameValidator.validate(name.text.toString())) {
                    Toast.makeText(
                        this,
                        "Imię nie może zawierać spacji oraz\n musi zaczynać się wielką literą!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                } else if (!LastNameValidator.validate(lastname.text.toString())) {
                    Toast.makeText(
                        this,
                        "Nazwisko nie może zawierać spacji oraz\n musi zaczynać się wielką literą!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }

                if (isCorrect && registerUser()) {
                    Toast.makeText(
                        this,
                        "Dziękujemy za rejestrację! Po wpłaceniu opłaty startowej konto zostanie utworzone.",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}