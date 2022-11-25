package com.dreamteam.marchapp.logic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dreamteam.marchapp.R

class Organisatormain2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organisatormain2)

        val btnForward = findViewById<Button>(R.id.btnForward)
        val modifyPoints = findViewById<Button>(R.id.modify_points)
        val modifyTracks = findViewById<Button>(R.id.modify_tracks)
        val makeBackup = findViewById<Button>(R.id.make_backup)
        val modifyPerData = findViewById<Button>(R.id.modify_par_data)

        btnForward.setOnClickListener{
            val Intent = Intent(this, OrganisatorMain::class.java)
            startActivity(Intent)
        }

        modifyPoints.setOnClickListener{
            val Intent = Intent(this, ModifyPoints::class.java)
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