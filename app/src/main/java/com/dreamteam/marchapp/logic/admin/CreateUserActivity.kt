package com.dreamteam.marchapp.logic.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.JDBCConnector
import com.dreamteam.marchapp.logic.config.PasswordEncoder
import com.dreamteam.marchapp.logic.shared.CodeQr.createCode
import com.dreamteam.marchapp.logic.validation.NameValidator
import com.dreamteam.marchapp.logic.validation.PasswordValidator
import com.dreamteam.marchapp.logic.validation.LastNameValidator
import com.dreamteam.marchapp.logic.validation.UsernameValidator

class CreateUserActivity : AppCompatActivity() {
    var connector = JDBCConnector
    /**
     * Invoked when all data is checked and we need a query to create new participant account
     */
    fun registerUser(){

        val username = findViewById<TextView>(R.id.username)
        val password = findViewById<TextView>(R.id.password)
        val name = findViewById<TextView>(R.id.name)
        val lastname = findViewById<TextView>(R.id.lastname)

        connector.startConnection()
        connector.prepareQuery("select * from role where nazwa = 'Uczestnik';")
        connector.executeQuery()
        var usrRoleId = -1
        try {
            usrRoleId = connector.getColInts(1)[0]
        } catch (e : Exception){
            throw Exception("Nie istnieje rola Uczestnika!")
        }

        val hashedPass = PasswordEncoder.hash(password.text.toString())

        //tworzenie konta w aplikacji
        try {
            connector.prepareQuery("insert into konta (login, hasło, rola_id) value (?, ?, ?);")
            connector.setStrVar(username.text.toString(), 1)
            connector.setStrVar(hashedPass, 2)
            connector.setIntVar(usrRoleId, 3)
            connector.executeQuery()
        } catch (e : Exception){
            e.printStackTrace()
        }
        connector.closeQuery()

        //znalezienie id nowoutworzonego konta
        connector.prepareQuery("select id_konta from konta where login = ? and hasło = ? ;")
        connector.setStrVar(username.text.toString(), 1)
        connector.setStrVar(hashedPass, 2)
        connector.executeQuery()
        val accountID = connector.getColInts(1)[0]
        connector.closeQuery()

        val qrCode = createCode()
        connector.startConnection()

        //tworzenie wpisu w bazie danych uczestników
        connector.prepareQuery("insert into uczestnicy (id_konta, imie, nazwisko, pseudonim, kod_qr) value (?, ?, ?, ?, ?) ;")
        connector.setIntVar(accountID, 1)
        connector.setStrVar(name.text.toString(), 2)
        connector.setStrVar(lastname.text.toString(), 3)
        connector.setStrVar(username.text.toString(), 4)    //na razie pseudonim taki jak imię
        connector.setStrVar(qrCode, 5)
        connector.executeQuery()
        connector.closeQuery()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)


        val username = findViewById<TextView>(R.id.username)
        val password = findViewById<TextView>(R.id.password)
        val repPassword = findViewById<TextView>(R.id.repeatPassword)
        val name = findViewById<TextView>(R.id.name)
        val lastname = findViewById<TextView>(R.id.lastname)
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val backBtn = findViewById<Button>(R.id.btnBack)

        backBtn.setOnClickListener {
            val Intent = Intent(this, AdministratorMain::class.java)
            startActivity(Intent)
        }

        registerBtn.setOnClickListener {
            if (username.text.isNullOrBlank() || password.text.isNullOrBlank() ||
                repPassword.text.isNullOrBlank() || name.text.isNullOrBlank() ||
                lastname.text.isNullOrBlank()
            ) {
                Toast.makeText(this, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
            } else {
                var isCorrect = true
                var loginTaken = true

                //Tu lecą zapytania do bazy
                //1. Zapytanie o to czy dana nazwa użytkownika jest zajęta,
                connector.startConnection()
                connector.prepareQuery("select * from konta where login = ?")
                connector.setStrVar(username.text.toString(), 1)
                connector.executeQuery()
                try {
                    connector.getAnswer()
                } catch(e: Exception){
                    loginTaken = false
                }
                //(zależy od tego jak będziemy to sprawdzać).

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
                } else if (!NameValidator.validate(name.text.toString())) {
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

                //Po rejestracji wracam do ekranu głównego administratora.
                if (isCorrect) {
                    registerUser()
                    Toast.makeText(
                        this,
                        "Rejestracja przebiegła poprawnie!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, AdministratorMain::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}