package com.example.easycash.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.easycash.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Mendapatkan referensi ke tombol dari layout
        val btnAddTransaksi = findViewById<Button>(R.id.btnAddTransaksi)
        val btnProductList = findViewById<Button>(R.id.btnProductList)
        val btnLaporan = findViewById<Button>(R.id.btnLaporan)
        val btnAboutUs = findViewById<Button>(R.id.btnAboutUs)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Mengatur OnClickListener untuk masing-masing tombol

        // Menambah Transaksi
        btnAddTransaksi.setOnClickListener {
            val intent = Intent(this, AddTransaksiActivity::class.java)
            startActivity(intent)
        }

        // Melihat Daftar Produk
        btnProductList.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
        }

        // Melihat Laporan
        btnLaporan.setOnClickListener {
            val intent = Intent(this, LaporanActivity::class.java)
            startActivity(intent)
        }

        // About Us
        btnAboutUs.setOnClickListener {
            val intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close the MainActivity
        }

        // Logout
        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close the MainActivity
        }
    }
}
