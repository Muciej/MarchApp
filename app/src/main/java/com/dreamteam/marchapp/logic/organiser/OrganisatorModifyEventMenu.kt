package com.dreamteam.marchapp.logic.organiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dreamteam.marchapp.R

class OrganisatorModifyEventMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organisator_modify_event_menu)

        val btnForward = findViewById<Button>(R.id.btnForward)
        val modifyPoints = findViewById<Button>(R.id.modify_points)
        val addPoints = findViewById<Button>(R.id.add_points)
        val modifyTracks = findViewById<Button>(R.id.modify_tracks)
        val makeBackup = findViewById<Button>(R.id.make_backup)
        val modifyPerData = findViewById<Button>(R.id.modify_par_data)

        btnForward.setOnClickListener{
            val Intent = Intent(this, OrganisatorMain::class.java)
            startActivity(Intent)
        }

        modifyPoints.setOnClickListener{
            val Intent = Intent(this, ShowAndEditPoints::class.java)
            startActivity(Intent)
        }

        addPoints.setOnClickListener{
            val Intent = Intent(this, CreatePointActivity::class.java)
            startActivity(Intent)
        }

        modifyTracks.setOnClickListener{
            val Intent = Intent(this, ModifyTracks::class.java)
            startActivity(Intent)
        }

        makeBackup.setOnClickListener{
            val Intent = Intent(this, MakeBackupOfMarchDatabase::class.java)
            startActivity(Intent)
        }

        modifyPerData.setOnClickListener{
            val Intent = Intent(this, ModifyParticipantsData::class.java)
            startActivity(Intent)
        }
    }
}