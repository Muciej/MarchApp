package com.dreamteam.marchapp.logic.organiser

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

class CreateAdminInMarchAccount : AppCompatActivity() {

    val connector = JDBCConnector

    /**
     * Invoked when data is check for valid values and
     * we want to send query to database
     */
    fun registerAdmin(username : String, name : String, lastname : String, password : String, email : String, phoneNr : String) : Boolean{
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
        connector.prepareQuery("select * from role where nazwa = 'Administrator';")
        var usrRoleId = -1
        try {
            connector.executeQuery()
            usrRoleId = connector.getColInts(1)[0]

        } catch (e : Exception){
            Toast.makeText(
                this,
                "Nie została zdefiniowana rola administratora!",
                Toast.LENGTH_SHORT
            ).show()
            return false;
        }

        val hashedPass = PasswordEncoder.hash(password)

        try {
            //tworzenie konta w aplikacji
            connector.prepareQuery("insert into konta (login, hasło, rola_id) value (?, ?, ?);")
            connector.setStrVar(username, 1)
            connector.setStrVar(hashedPass, 2)
            connector.setIntVar(usrRoleId, 3)
            connector.executeQuery()
            connector.closeQuery()
            println("Konto poszło")

            //znalezienie id nowoutworzonego konta
            connector.prepareQuery("select id_konta from konta where login = ? and hasło = ?;")
            connector.setStrVar(username, 1)
            connector.setStrVar(hashedPass, 2)
            connector.executeQuery()
            val accountID = connector.getColInts(1)[0]
            connector.closeQuery()
            println("Id roli admina: $accountID")


            //tworzenie wpisu w bazie danych personelu
            connector.prepareQuery("insert into personel (id_konta, imie, nazwisko, nr_telefonu, mail) value (?, ?, ?, ?, ?);")
            connector.setIntVar(accountID, 1)
            connector.setStrVar(name, 2)
            connector.setStrVar(lastname, 3)
            connector.setStrVar(phoneNr, 4)
            connector.setStrVar(email, 5)
            connector.executeQuery()
            connector.closeQuery()
            println("Utworzone w personel")

            return true;
        } catch (e : Exception){
            Toast.makeText(
                this,
                "Nie udało się dodać administratora!",
                Toast.LENGTH_SHORT
            ).show()
            return false;
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_admin_in_march_account)

        val name = findViewById<TextView>(R.id.name)
        val lastname = findViewById<TextView>(R.id.lastname)
        val username = findViewById<TextView>(R.id.username)
        val password = findViewById<TextView>(R.id.password)
        val repPassword = findViewById<TextView>(R.id.repeatPassword)
        val email = findViewById<TextView>(R.id.email)
        val phoneNr = findViewById<TextView>(R.id.phone)
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
                var loginTaken = true

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
                } else if(!UsernameValidator.validate(username.text.toString())) {
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
                } else if (password.text.toString() != repPassword.text.toString()) {
                    Toast.makeText(
                        this,
                        "Wprowadzone hasła muszą być identyczne!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }
                else if (!EmailValidator.validate(email.text.toString())) {
                    Toast.makeText(
                        this,
                        "Nieprawidłowy format email!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }
                else if (!PhoneValidator.validate(phoneNr.text.toString())) {
                    Toast.makeText(
                        this,
                        "Nieprawidłowy format numeru!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }

                val str_username = username.text.toString()
                val str_name = name.text.toString()
                val str_lastname = lastname.text.toString()
                val str_password = password.text.toString()
                val str_email = email.text.toString()
                val str_phonenr = phoneNr.text.toString()

                //Po rejestracji wracam do ekranu głównego administratora.
                if (isCorrect && registerAdmin(str_username, str_name, str_lastname, str_password, str_email, str_phonenr))
                {
                    Toast.makeText(
                        this,
                        "Rejestracja przebiegła poprawnie!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val Intent = Intent(this, OrganisatorMain::class.java)
                    startActivity(Intent)
                }
            }
        }
    }
}