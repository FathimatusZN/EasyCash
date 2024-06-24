package com.example.easycash.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.easycash.R

class AboutUsActivity : AppCompatActivity() {
    private lateinit var icBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        // Listener untuk tombol kembali
        icBack = findViewById(R.id.ic_back)
        icBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            // Set the flags to clear the task and start a new task
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish() // Tutup AboutUsActivity
        }
    }
}
