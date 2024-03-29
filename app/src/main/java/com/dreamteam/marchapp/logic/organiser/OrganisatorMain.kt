package com.dreamteam.marchapp.logic.organiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.JDBCConnector
import com.dreamteam.marchapp.logic.shared.ChangePassword
import com.dreamteam.marchapp.logic.shared.ChooseMarchActivity
import com.dreamteam.marchapp.logic.shared.ShowAndEditParticipant
import com.dreamteam.marchapp.logic.shared.ViewSt
import kotlinx.android.synthetic.main.activity_organisator_main.*

class OrganisatorMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organisator_main)

        /*

        Jeśli chcemy gradientowe tło to wystarczy odkomentować

        val l = findViewById<RelativeLayout>(R.id.orgLayout)
        val ll = l.background as AnimationDrawable
        ll.setEnterFadeDuration(2500)
        ll.setExitFadeDuration(2500)
        ll.start()

         */

        val modify_event_btn = findViewById<Button>(R.id.modify_event)
        val create_adm_acc = findViewById<Button>(R.id.create_adm_acc)
        val view_statistics = findViewById<Button>(R.id.view_statistics)
        val volunteers = findViewById<Button>(R.id.volunteers)
        val change_org_Pass = findViewById<Button>(R.id.change_org_Pass)
        val log_out_from_org_account = findViewById<Button>(R.id.log_out_from_org_account)


        modify_event_btn.setOnClickListener {
            val Intent = Intent(this, OrganisatorModifyEventMenu::class.java)
            startActivity(Intent)
        }

        create_adm_acc.setOnClickListener {
            val Intent = Intent(this, CreateAdminInMarchAccount::class.java)
            startActivity(Intent)
        }

        view_statistics.setOnClickListener {
            val Intent = Intent(this, ViewSt::class.java)
            startActivity(Intent)
        }
//tu
        participant.setOnClickListener{
            val intent = Intent(this, ShowAndEditParticipant::class.java)
            intent.putExtra("accessLevel", "Organiser")
            startActivity(intent)
        }

        volunteers.setOnClickListener{
            val Intent = Intent(this, ShowAndEditVolunteers::class.java)
            Intent.putExtra("accessLevel", "Organiser")
            startActivity(Intent)
        }

        change_org_Pass.setOnClickListener{
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }

        log_out_from_org_account.setOnClickListener{
            JDBCConnector.closeConnection()
            val Intent = Intent(this, ChooseMarchActivity::class.java)
            startActivity(Intent)
        }
    }
}