package com.dreamteam.marchapp.logic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.dreamteam.marchapp.R

class ModifyPoints : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_points)

        val btnBack = findViewById<Button>(R.id.btn_back)
        val pointName = findViewById<EditText>(R.id.pointName)
        val pointBtn = findViewById<Button>(R.id.pointBtn)
        val delPoint = findViewById<EditText>(R.id.delPoint)
        val deleteBtn = findViewById<Button>(R.id.delete_btn)

        btnBack.setOnClickListener{
            val Intent = Intent(this, Organisatormain2::class.java)
            startActivity(Intent)
        }


        pointBtn.setOnClickListener {
            if (pointName.text.isNullOrBlank()){
                Toast.makeText(this, "Wymagany jest wybór punktu do modyfikacji", Toast.LENGTH_SHORT)
                    .show()
            }
            else{
                Toast.makeText(this, "Tu wyskoczy możliwość modyfikacji punktu", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        deleteBtn.setOnClickListener {
            if (delPoint.text.isNullOrBlank()){
                Toast.makeText(this, "Wymagany jest wybór punktu do usunięcia", Toast.LENGTH_SHORT)
                    .show()
            }
            else{
                val Intent = Intent(this, DeletePoint::class.java)
                startActivity(Intent)
            }
        }
    }
}