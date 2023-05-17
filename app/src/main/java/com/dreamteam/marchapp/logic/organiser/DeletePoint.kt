package com.dreamteam.marchapp.logic.organiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.dreamteam.marchapp.R

class DeletePoint : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_point)

        val btnBack = findViewById<Button>(R.id.btn_back)
        val yesBtn = findViewById<Button>(R.id.yes_btn)
        val noBtn = findViewById<Button>(R.id.no_btn)

        btnBack.setOnClickListener{
            val Intent = Intent(this, OrganisatorModifyEventMenu::class.java)
            startActivity(Intent)
        }


        yesBtn.setOnClickListener {
            //TODO: Tu będzie lecieć zapytanie odnośnie usunięcia punktu
            Toast.makeText(this, "Punkt został usunięty", Toast.LENGTH_SHORT)
                .show()
            val Intent = Intent(this, OrganisatorModifyEventMenu::class.java)
            startActivity(Intent)
        }

        noBtn.setOnClickListener {
            val Intent = Intent(this, OrganisatorModifyEventMenu::class.java)
            startActivity(Intent)
        }


    }
}