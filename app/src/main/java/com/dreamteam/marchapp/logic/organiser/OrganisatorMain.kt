package com.dreamteam.marchapp.logic.organiser

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.logic.shared.ChooseMarchActivity

class OrganisatorMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organisator_main)

        val l = findViewById<RelativeLayout>(R.id.orgLayout)
        val ll = l.background as AnimationDrawable
        ll.setEnterFadeDuration(2500)
        ll.setExitFadeDuration(2500)
        ll.start()

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

        create_adm_acc.setOnClickListener {
            val Intent = Intent(this, CreateAdminInMarchAccount::class.java)
            startActivity(Intent)
        }

        view_statistics.setOnClickListener {
            val Intent = Intent(this, ViewStAsOrganiser::class.java)
            startActivity(Intent)
        }

        volunteers.setOnClickListener{
            val Intent = Intent(this, ShowVolunteers::class.java)
            startActivity(Intent)
        }

        change_org_Pass.setOnClickListener{
            Toast.makeText(this, "Tu będzie ekran zmiany hasła", Toast.LENGTH_SHORT).show()
        }

        log_out_from_org_account.setOnClickListener{
            val Intent = Intent(this, ChooseMarchActivity::class.java)
            startActivity(Intent)
        }

    }
}