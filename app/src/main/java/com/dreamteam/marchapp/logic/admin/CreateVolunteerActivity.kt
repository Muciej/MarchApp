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
import com.dreamteam.marchapp.logic.validation.EmailValidator
import com.dreamteam.marchapp.logic.validation.PasswordValidator
import com.dreamteam.marchapp.logic.validation.PhoneValidator
import com.dreamteam.marchapp.logic.validation.UsernameValidator

class CreateVolunteerActivity : AppCompatActivity() {
    var connector = JDBCConnector
    /**
     * Invoked when all data is checked and we need a query to create new participant account
     */
    fun registerUser() : Boolean{
        val username = findViewById<TextView>(R.id.username)
        val name = findViewById<TextView>(R.id.name)
        val lastname = findViewById<TextView>(R.id.lastname)
        val password = findViewById<TextView>(R.id.password)
        val email = findViewById<TextView>(R.id.email)
        val phoneNr = findViewById<TextView>(R.id.number)

        try {
            connector.startConnection()
        } catch (e : Exception){
            Toast.makeText(
                this,
                "Nie można nawiązać połączenia z bazą!",
                Toast.LENGTH_SHORT
            ).show()
            return false;
        }
        connector.prepareQuery("select * from role where nazwa = 'Wolontariusz';")
        var usrRoleId = -1
        try {
            connector.executeQuery()
            usrRoleId = connector.getColInts(1)[0]

        } catch (e : Exception){
            Toast.makeText(
                this,
                "Nie została zdefiniowana rola wolontariusza!",
                Toast.LENGTH_SHORT
            ).show()
            return false;
        }

        val hashedPass = PasswordEncoder.hash(password.text.toString())
        try {
            //tworzenie konta w aplikacji
            connector.prepareQuery("insert into konta (login, hasło, rola_id) value (?, ?, ?);")
            connector.setStrVar(username.text.toString(), 1)
            connector.setStrVar(hashedPass, 2)
            connector.setIntVar(usrRoleId, 3)
            connector.executeQuery()
            connector.closeQuery()

            //znalezienie id nowoutworzonego konta
            connector.prepareQuery("select id_konta from konta where login = ? and hasło = ?;")
            connector.setStrVar(username.text.toString(), 1)
            connector.setStrVar(hashedPass, 2)
            connector.executeQuery()
            val accountID = connector.getColInts(1)[0]
            connector.closeQuery()

            //tworzenie wpisu w bazie danych personelu
            connector.prepareQuery("insert into personel (id_konta, imie, nazwisko, nr_telefonu, mail) value (?, ?, ?, ?, ?);")
            connector.setIntVar(accountID, 1)
            connector.setStrVar(name.text.toString(), 2)
            connector.setStrVar(lastname.text.toString(), 3)
            connector.setStrVar(phoneNr.text.toString(), 4)
            connector.setStrVar(email.text.toString(), 5)
            connector.executeQuery()
            connector.closeQuery()

            return true;
        } catch (e : Exception){
            Toast.makeText(
                this,
                "Nie udało się dodać wolontariusza!",
                Toast.LENGTH_SHORT
            ).show()
            return false;
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_volunteer)

        val username = findViewById<TextView>(R.id.username)
        val name = findViewById<TextView>(R.id.name)
        val lastname = findViewById<TextView>(R.id.lastname)
        val password = findViewById<TextView>(R.id.password)
        val repPassword = findViewById<TextView>(R.id.repeatPassword)
        val email = findViewById<TextView>(R.id.email)
        val phoneNr = findViewById<TextView>(R.id.number)
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val backBtn = findViewById<Button>(R.id.btnBack)

        backBtn.setOnClickListener{
            val Intent = Intent(this, AdministratorMain::class.java)
            startActivity(Intent)
        }

        registerBtn.setOnClickListener{
            if (name.text.isNullOrBlank() || lastname.text.isNullOrBlank() || username.text.isNullOrBlank() || password.text.isNullOrBlank() ||
                repPassword.text.isNullOrBlank() || email.text.isNullOrBlank() ||
                phoneNr.text.isNullOrBlank()
            )
            {
                Toast.makeText(this, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
            }
            else
            {
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
                }
//                else if(!NameValidator.validate(name.text.toString())) {
//                    Toast.makeText(
//                        this,
//                        "Nieprawidlowy format imienia (imie nie może zawierać znaków specjalnych i zaczynać się z małej litery)",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    isCorrect = false
//
//                }
//                else if(!LastNameValidator.validate(lastname.text.toString())) {
//                    Toast.makeText(
//                        this,
//                        "Nieprawidlowy format nazwiska (naziwsko nie może zawierać znaków specjalnych i zaczynać się z małej litery)",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    isCorrect = false
//                }

                else if (!PasswordValidator.validate(password.text.toString())) {
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
                if (isCorrect && registerUser())
                {
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