package com.dreamteam.marchapp.logic.shared

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.DataViewModel
import com.dreamteam.marchapp.database.dataclasses.Account
import com.dreamteam.marchapp.database.dataclasses.Roles
import com.dreamteam.marchapp.logic.organiser.OrganisatorMain
import com.dreamteam.marchapp.logic.volunteer.VolunteerMain
import com.dreamteam.marchapp.logic.admin.AdministratorMain
import com.dreamteam.marchapp.logic.config.PasswordEncoder
import com.dreamteam.marchapp.logic.participant.RegisterParticipant
import java.util.Vector

class LoginActivity : AppCompatActivity() {

    private lateinit var dataViewModel: DataViewModel
    private var btnPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        dataViewModel.loggedAcount.observe(this, Observer {
            newLoggedAccount -> updateLoggedUser(newLoggedAccount)
        })
        dataViewModel.logoutUser()

        //CodeQr.createCode()

        val username = findViewById<TextView>(R.id.username)
        val password = findViewById<TextView>(R.id.password)
        val btnSign = findViewById<Button>(R.id.signbtn)
        val backBtn = findViewById<Button>(R.id.btnBack)
        val btnRegister = findViewById<Button>(R.id.registerbtn)

        backBtn.setOnClickListener{
            val Intent = Intent(this, ChooseMarchActivity::class.java)
            startActivity(Intent)
        }

        btnSign.setOnClickListener {
            if (username.text.isNullOrBlank() || password.text.isNullOrBlank()) {
                Toast.makeText(this, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
            } else {
                val hashedPassword = PasswordEncoder.hash(password.text.toString())
                btnPressed = true
                dataViewModel.loginUser(username.text.toString(), hashedPassword)
            }
        }

        btnRegister.setOnClickListener{
            val Intent = Intent(this, RegisterParticipant::class.java)
            startActivity(Intent)
        }
    }

    private fun updateLoggedUser(newLoggedUser: Account?){
        if (newLoggedUser != null) {
            var intent: Intent? = null
            when(newLoggedUser.role){
                Roles.ORGANISER -> intent = Intent(this, OrganisatorMain::class.java)
                Roles.VOLOUNTEER -> intent = Intent(this, VolunteerMain::class.java)
                Roles.PARTICIPANT -> Toast.makeText(this, "Zalogowano jako uczestnik!", Toast.LENGTH_SHORT).show()
                Roles.ADMIN -> intent = Intent(this, AdministratorMain::class.java)
                Roles.UNKNOWN -> Toast.makeText(this, "Konto nie zostało poprawnie utworzone!", Toast.LENGTH_SHORT).show()
            }
            if(intent != null)
                startActivity(intent)
        } else if(btnPressed){
            Toast.makeText(this, "Niepoprawne dane logowania!", Toast.LENGTH_SHORT).show()
            btnPressed = false
        }
    }

}