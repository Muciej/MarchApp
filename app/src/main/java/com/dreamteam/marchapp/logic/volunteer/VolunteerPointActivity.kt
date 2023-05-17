package com.dreamteam.marchapp.logic.volunteer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.JDBCConnector

class VolunteerPointActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_point)

        var connector = JDBCConnector
        val pointName = findViewById<TextView>(R.id.pointName)
        val pointBtn = findViewById<Button>(R.id.pointBtn)
        val backBtn = findViewById<Button>(R.id.btnBack)

        fun modifyPoint(name : String){
            connector.startConnection()
            connector.prepareQuery("select id from punkty_kontrolne where nazwa = '$name' ;")
            var pointId = 0
            try{
                connector.executeQuery()
                connector.moveRow()
                val ans = connector.getCurrRow(1)
                pointId = ans[0].toInt()
            } catch (e : Exception){
                Toast.makeText(this, "Nie ma punktu o takiej nazwie!", Toast.LENGTH_SHORT).show()
            }
            connector.closeQuery()

            connector.startConnection()
            connector.prepareQuery("select id_wolontariusza from personel p ")
            connector.setIntVar(connector.getCurrentUserID(),1)
            var volId = 0
            try{
                connector.executeQuery()
                volId = connector.getColInts(0)[0]
            } catch (e : Exception){
                Toast.makeText(this, "Nie udało się znaleźć wolontariusza od takim id konta!", Toast.LENGTH_SHORT).show()
            }
            connector.closeQuery()

            connector.startConnection()
            connector.prepareQuery("DELETE FROM wolontariusz_punkt WHERE id_wolontariusza = ? ;")
            connector.setIntVar(volId, 1)
            connector.executeQuery()
            connector.closeQuery()

            connector.startConnection()
            connector.prepareQuery("insert into wolontariusz_punkt (id_wolontariusza, id_punktu) value (?, ?) ; ")
            connector.setIntVar(1, volId)
            connector.setIntVar(2, pointId)
            try{
                connector.executeQuery()
            } catch (e : Exception){
                Toast.makeText(this, "Nie udało się przypisać wolontariusza do punktu!", Toast.LENGTH_SHORT).show()
            }
            connector.closeQuery()
        }

        backBtn.setOnClickListener{
            val Intent = Intent(this, VolunteerMain::class.java)
            startActivity(Intent)
        }

        pointBtn.setOnClickListener{
            if (pointName.text.isNullOrBlank()) {
                Toast.makeText(this, "Musisz wybrać punkt!", Toast.LENGTH_SHORT).show()
            }
            else {
                try{
                    modifyPoint(pointName.text.toString())
                    Toast.makeText(this, "Punkt został przypisany!", Toast.LENGTH_SHORT).show()
                } catch (e : Exception){
                    Toast.makeText(this, "Nie udało się przypisać punktu!", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }


}