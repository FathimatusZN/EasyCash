package com.example.easycash.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easycash.database.DatabaseHelper
import com.example.easycash.R
import com.example.easycash.adapters.LaporanAdapter
import com.example.easycash.models.Payment

class LaporanActivity : AppCompatActivity() {

    // Deklarasi variabel untuk elemen UI dan DatabaseHelper
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var laporanAdapter: LaporanAdapter
    private lateinit var rvLaporan: RecyclerView
    private lateinit var icBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        dbHelper = DatabaseHelper(this)

        // Inisialisasi RecyclerView
        rvLaporan = findViewById(R.id.rv_pendapatan)
        rvLaporan.layoutManager = LinearLayoutManager(this)
        icBack = findViewById(R.id.ic_back)

        // Set up adapter
        val laporanList = dbHelper.getAllLaporan()
        laporanAdapter = LaporanAdapter(laporanList)
        rvLaporan.adapter = laporanAdapter

        // Update total pendapatan text view
        val totalPendapatanTextView: TextView = findViewById(R.id.totalPendapatanTextView)
        val totalPendapatan = calculateTotalTransaksi(laporanList)
        totalPendapatanTextView.text = "Rp. $totalPendapatan"
        icBack.setOnClickListener {
            finish()
        }
    }

    // Fungsi untuk menjumlahkan total transaksi
    private fun calculateTotalTransaksi(laporanList: List<Payment>): Double {
        var total = 0.0
        for (laporan in laporanList) {
            total += laporan.total_bayar
        }
        return total
    }
}
