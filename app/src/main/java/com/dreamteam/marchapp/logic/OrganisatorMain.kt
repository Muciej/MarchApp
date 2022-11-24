package com.dreamteam.marchapp.logic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.dreamteam.marchapp.R

class OrganisatorMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organisator_main)

        val btn_back = findViewById<Button>(R.id.btn_back)
        val modify_event_btn = findViewById<Button>(R.id.modify_event)
        val create_adm_acc = findViewById<Button>(R.id.create_adm_acc)
        val view_statistics = findViewById<Button>(R.id.view_statistics)
        val volunteers = findViewById<Button>(R.id.volunteers)
        val change_org_Pass = findViewById<Button>(R.id.change_org_Pass)
        val log_out_from_org_account = findViewById<Button>(R.id.log_out_from_org_account)


        modify_event_btn.setOnClickListener {
            val Intent = Intent(this, Organisatormain2::class.java)
            startActivity(Intent)
        }

    }
}