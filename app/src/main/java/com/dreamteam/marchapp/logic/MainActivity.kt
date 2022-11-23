package com.dreamteam.marchapp.logic
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dreamteam.marchapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val buttonSignUpp = findViewById<Button>(R.id.buttonSignUp)
        buttonSignUpp.setOnClickListener{
            val Intent = Intent(this, LoginActivity::class.java)
            startActivity(Intent)
        }
    }
}