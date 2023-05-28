package com.dreamteam.marchapp.logic.organiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.DBConnector
import com.dreamteam.marchapp.database.JDBCConnector

class ModifyPoints : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_points)

        val btnBack = findViewById<Button>(R.id.btn_back)
        val pointName = findViewById<EditText>(R.id.pointName)
        val pointBtn = findViewById<Button>(R.id.pointBtn)
        val delPoint = findViewById<EditText>(R.id.delPoint)
        val deleteBtn = findViewById<Button>(R.id.delete_btn)
        val pointlist = findViewById<RecyclerView>(R.id.point_list)


        val connector: DBConnector = JDBCConnector
        connector.startConnection()
        connector.prepareQuery("select nazwa from punkty_kontrolne;")
        connector.executeQuery()
        val names = connector.getCol(1)
        //TODO a jak to dodać do listy?

        btnBack.setOnClickListener{
            val Intent = Intent(this, OrganisatorModifyEventMenu::class.java)
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