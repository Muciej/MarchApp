package com.dreamteam.marchapp.logic.organiser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.logic.shared.ViewSt

class SelectPointToFollow: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    //From the DB
    private val controlPoints = arrayOf("first", "second", "third", "...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_point_to_follow)

        val myspinner= findViewById<Spinner>(R.id.spinner2)
        val btnChoose = findViewById<Button>(R.id.chooseBtn)
        val backBtn = findViewById<Button>(R.id.btnBack)

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, controlPoints)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(myspinner) {
            adapter = arrayAdapter
            setSelection(0, false)
            onItemSelectedListener = this@SelectPointToFollow
            prompt = "Select control point"
            gravity = Gravity.CENTER
        }

        backBtn.setOnClickListener {
//            val intent = Intent(this, ViewStAsOrganiser::class.java)
            val intent = Intent(this, ViewSt::class.java)
            startActivity(intent)
        }

        btnChoose.setOnClickListener{
            val intent = Intent(this, FollowPointStatus::class.java)
            startActivity(intent)
        }

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        showToast(message = "Wybrano punkt: ${controlPoints[p2]}")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        showToast(message = "Nie wybrano punktu!")
    }

    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }
}