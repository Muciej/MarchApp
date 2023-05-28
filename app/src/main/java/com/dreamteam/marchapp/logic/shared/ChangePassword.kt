package com.dreamteam.marchapp.logic.shared

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.DataViewModel
import com.dreamteam.marchapp.database.dataclasses.Roles
import com.dreamteam.marchapp.logic.admin.AdministratorMain
import com.dreamteam.marchapp.logic.config.PasswordEncoder
import com.dreamteam.marchapp.logic.organiser.OrganisatorMain
import com.dreamteam.marchapp.logic.participant.RegisterParticipant
import com.dreamteam.marchapp.logic.validation.PasswordValidator
import com.dreamteam.marchapp.logic.volunteer.VolunteerMain


class ChangePassword : AppCompatActivity() {
    private lateinit var dataViewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]

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

                changePassword()

                //Po aktualizacji wracam do ekranu głównego administratora.
                if (isCorrect) {
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

    private fun changePassword() {
        val newPassword = findViewById<TextView>(R.id.newPassword)

        val hashedPass = PasswordEncoder.hash(newPassword.text.toString())
        dataViewModel.changeUserPassword(dataViewModel.loggedAcount.value!!.id, hashedPass)
    }

    private fun previousPage() {
        when (dataViewModel.loggedAcount.value!!.role) {
            Roles.ORGANISER -> {
                val intent = Intent(this, OrganisatorMain::class.java)
                startActivity(intent)
            }
            Roles.VOLOUNTEER -> {
                val intent = Intent(this, VolunteerMain::class.java)
                startActivity(intent)
            }
            Roles.PARTICIPANT -> {
                val intent = Intent(this, RegisterParticipant::class.java)
                startActivity(intent)
            }
            Roles.ADMIN -> {
                val intent = Intent(this, AdministratorMain::class.java)
                startActivity(intent)
            }
            else -> {
                Toast.makeText(
                    this,
                    "Nie posiadasz przypisanej roli",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

