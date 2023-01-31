package com.dreamteam.marchapp.logic.shared

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.DBConnector
import com.dreamteam.marchapp.database.JDBCConnector
import com.dreamteam.marchapp.logic.admin.AdministratorMain
import com.dreamteam.marchapp.logic.config.PasswordEncoder
import com.dreamteam.marchapp.logic.organiser.OrganisatorMain
import com.dreamteam.marchapp.logic.participant.RegisterParticipant
import com.dreamteam.marchapp.logic.validation.EmailValidator
import com.dreamteam.marchapp.logic.validation.PasswordValidator
import com.dreamteam.marchapp.logic.validation.PhoneValidator
import com.dreamteam.marchapp.logic.volunteer.VolunteerMain


class ChangePassword : AppCompatActivity() {
    var connector: DBConnector = JDBCConnector

    fun changePassword() : Boolean {
        val newPassword = findViewById<TextView>(R.id.newPassword)

        connector.startConnection()
        var CurrentUserID = connector.getCurrentUserID()
        val hashedPass = PasswordEncoder.hash(newPassword.text.toString())

        try {
            connector.prepareQuery("UPDATE konta SET hasło = '${hashedPass}' WHERE id_konta LIKE '${CurrentUserID}';")
            connector.executeQuery()
        } catch (e: Exception) {
            Toast.makeText(this, "Problem ze polaczeniem\n z baza danych", Toast.LENGTH_SHORT).show()
            return false
        }
        connector.closeQuery()
        connector.closeConnection()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val oldPassword = findViewById<TextView>(R.id.oldPassword)
        val newPassword = findViewById<TextView>(R.id.newPassword)
        val repeatPassword = findViewById<TextView>(R.id.repeatPassword)
        val changePassButt = findViewById<Button>(R.id.changePassButt)
        val backBtn = findViewById<Button>(R.id.btnBack)


        backBtn.setOnClickListener {
            previousPage()
        }

        changePassButt.setOnClickListener {
            if (oldPassword.text.isNullOrBlank() || newPassword.text.isNullOrBlank() ||
                repeatPassword.text.isNullOrBlank()
            ) {
                Toast.makeText(this, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
            } else {
                var isCorrect = true
                if (!PasswordValidator.validate(newPassword.text.toString())) {
                    Toast.makeText(
                        this,
                        "Nieprawidlowa dlugosc hasla (8-64 znaki",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                } else if ((newPassword.text.toString() != repeatPassword.text.toString())) {
                    Toast.makeText(
                        this,
                        "Wprowadzone hasła muszą być identyczne!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCorrect = false
                }

                //Po aktualizacji wracam do ekranu głównego administratora.
                if (isCorrect && changePassword()) {
                    Toast.makeText(
                        this,
                        "Zmiana hasła przebiegła poprawnie!",
                        Toast.LENGTH_SHORT
                    ).show()
                    // powrot do poprzedniego widoku
                    previousPage()
                }
            }
        }
    }

    fun previousPage(){

        connector.startConnection()
        var CurrentUserID = connector.getCurrentUserID()
        connector.prepareQuery("select rola_id from konta where id_konta like '${CurrentUserID}';")
        connector.executeQuery()
        var rola = Integer.parseInt(connector.getCol(1)[0])
        connector.closeQuery()
        connector.closeConnection()

        when (rola) {
            1 -> {
                val intent = Intent(this, OrganisatorMain::class.java)
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(this, VolunteerMain::class.java)
                startActivity(intent)
            }
            3 -> {
                // TODO tu powinno odnosić do widoku uczestnika, ale jak na razie on nie istnieje xD
                val intent = Intent(this, RegisterParticipant::class.java)
                startActivity(intent)
            }
            4 -> {
                val intent = Intent(this, AdministratorMain::class.java)
                startActivity(intent)
            } else -> {
                Toast.makeText(
                    this,
                    "Nie posiadasz przypisanej roli",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}



