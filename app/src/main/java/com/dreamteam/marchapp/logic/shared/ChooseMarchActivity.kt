package com.dreamteam.marchapp.logic.shared

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.DBConnector
import com.dreamteam.marchapp.database.DataViewModel
import com.dreamteam.marchapp.database.JDBCConnector
import com.dreamteam.marchapp.database.dataclasses.Event
import java.util.Vector

class ChooseMarchActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var dataViewModel: DataViewModel
    var eventList: List<Event>? = null
    var dbNames = Vector<String>()
    var marchNames = Vector<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_march)
        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]

        val btnChoose = findViewById<Button>(R.id.chooseBtn)
        val backBtn = findViewById<Button>(R.id.btnForward)

        //przycisk powrotu
        backBtn.setOnClickListener{
            val Intent = Intent(this, LoginActivity::class.java)
            startActivity(Intent)
        }

        // przycisk zatwierdzenia wybranego marszu
        btnChoose.setOnClickListener{
            val Intent = Intent(this, LoginActivity::class.java)
            startActivity(Intent)
        }
    }

    fun marchesUpdated(events: List<Event>){
        eventList = events
        marchNames.clear()
        dbNames.clear()
        for(event in events){
            marchNames.add(event.event_name)
            dbNames.add(event.event_database)
        }
        updateSpinner()
    }

    private fun updateSpinner(){
        val myspinner= findViewById<Spinner>(R.id.spinner2)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, marchNames)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // tutaj w zaleznosci co wybraliśmy switchuje odpowiednią bazę z konkretnego marszu
        with(myspinner)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@ChooseMarchActivity
            prompt = "Select march"
            gravity = Gravity.CENTER
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        showToast(message = "spinner position:${p2} and march: ${dbNames[p2]}")
        dataViewModel.chooseEvent(eventList?.get(p2))
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        showToast(message = "Nothing selected")
    }
    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }
}