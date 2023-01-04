package com.dreamteam.marchapp.logic.shared

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.favre.lib.crypto.bcrypt.BCrypt
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.JDBCConnector
import com.dreamteam.marchapp.logic.organiser.OrganisatorMain
import com.dreamteam.marchapp.logic.volunteer.VolunteerMain
import com.dreamteam.marchapp.logic.admin.AdministratorMain

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //CodeQr.createCode()

        val connector = JDBCConnector
        val username = findViewById<TextView>(R.id.username)
        val password = findViewById<TextView>(R.id.password)
        val btnSign = findViewById<Button>(R.id.signbtn)
        val backBtn = findViewById<Button>(R.id.btnBack)

        backBtn.setOnClickListener{
            
            connector.closeConnection()
            val Intent = Intent(this, ChooseMarchActivity::class.java)
            startActivity(Intent)
        }

        btnSign.setOnClickListener {
            if (username.text.isNullOrBlank() || password.text.isNullOrBlank()) {
                Toast.makeText(this, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
            } else {
                var isCorrect = false
                //TODO: Po stworzeniu bazy danych pobrane haslo z bazy porownujemy z zahashowanym
                //na razie wyłączyłem hashowanie, bo jeszcze go nie ma w bazie
//                val hashedPassword: String =
//                    BCrypt.withDefaults().hashToString(12,password.text.toString().toCharArray())

                connector.startConnection()
                connector.prepareQuery("select r.poziom_uprawnień from konta k\n" +
                        "join role r on k.rola_id = r.id_roli\n" +
                        "where login = ? and hasło = ?;")
                connector.setStrVar(username.text.toString(), 1)
                connector.setStrVar(password.text.toString(), 2)
                connector.executeQuery()
                val ans = connector.getRow(1, 1)
                var intent: Intent? = null
                if(ans.size == 0)
                {
                    Toast.makeText(this, "Niepoprawne dane, spróbuj ponownie!", Toast.LENGTH_SHORT).show()
                    isCorrect = false
                }
                else{
                    when(ans[0]){
                        "organiser" -> intent = Intent(this, OrganisatorMain::class.java)
                        "volounteer"-> intent = Intent(this, VolunteerMain::class.java)
                        "participant" -> Toast.makeText(this, "Zalogowano jako uczestnik!", Toast.LENGTH_SHORT).show()
                        "admin" -> intent = Intent(this, AdministratorMain::class.java)
                        "register" -> {
                            //Todo jakiś panel rejestracji?
                        }
                    }
                }
                if(intent != null)
                    startActivity(intent)
            }
        }
    }

}