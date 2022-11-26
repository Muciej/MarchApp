package com.dreamteam.marchapp.logic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.dreamteam.marchapp.R

class MakeBackupOfMarchDatabase : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_backup_of_march_database)

        val btnBack = findViewById<Button>(R.id.btn_back)
        val backupButton = findViewById<Button>(R.id.backupButton)

        btnBack.setOnClickListener{
            val Intent = Intent(this, Organisatormain2::class.java)
            startActivity(Intent)
        }
        //TODO: tutaj znajdować się będzie zapytanie, tworzące backup bazy.
        //TODO: w tym miejscu będziemy także wykrywać czy backup jest możliwy, tzn. czy
        //TODO: mamy połączenie z bazą

        backupButton.setOnClickListener {
            Toast.makeText(this, "Backup został przeprowadzony", Toast.LENGTH_SHORT)
                .show()
        }
    }
}