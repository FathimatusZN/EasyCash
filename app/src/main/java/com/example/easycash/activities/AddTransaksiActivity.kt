package com.example.easycash.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easycash.R
import com.example.easycash.adapters.TransaksiAdapter
import com.example.easycash.database.DatabaseHelper
import com.example.easycash.models.Product
import java.io.Serializable

class AddTransaksiActivity : AppCompatActivity() {

    // Deklarasi variabel untuk elemen UI dan DatabaseHelper
    private lateinit var rvTransaksi: RecyclerView
    private lateinit var txtTotalPembayaran: TextView
    private lateinit var transaksiAdapter: TransaksiAdapter
    private lateinit var icBack: ImageView
    private lateinit var db: DatabaseHelper
    private val productQuantities = mutableMapOf<Product, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaksi)

        // Inisialisasi elemen UI
        icBack = findViewById(R.id.ic_back)
        rvTransaksi = findViewById(R.id.rv_transaksi)
        txtTotalPembayaran = findViewById(R.id.txtTotalPembayaran)

        // Inisialisasi DatabaseHelper
        db = DatabaseHelper(this)

        // Mengatur layout manager untuk RecyclerView
        rvTransaksi.layoutManager = LinearLayoutManager(this)
        val productList = db.getAllProducts()

        // Inisialisasi adapter dan set ke RecyclerView
        transaksiAdapter = TransaksiAdapter(productList) { product, quantity ->
            productQuantities[product] = quantity
            updateTotalPembayaran()
        }
        rvTransaksi.adapter = transaksiAdapter

        // Set listener untuk tombol bayar
        val btnBayar: Button = findViewById(R.id.btnBayar)
        btnBayar.setOnClickListener { handleBayarButton() }

        // Set listener untuk tombol kembali
        icBack.setOnClickListener { finish() }
    }

    // Fungsi untuk menangani tombol bayar
    private fun handleBayarButton() {
        try {
            val totalPembayaran = txtTotalPembayaran.text.toString().replace("Rp. ", "").toDouble()
            Log.d("AddTransaksiActivity", "Total Pembayaran: $totalPembayaran")
            val intent = Intent(this, BayarActivity::class.java).apply {
                putExtra("TOTAL_PEMBAYARAN", totalPembayaran)
                putExtra("PRODUCT_QUANTITIES", HashMap(productQuantities))  // Pass the product quantities
            }
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("AddTransaksiActivity", "Error parsing total pembayaran", e)
        }
    }

    // Fungsi untuk mengupdate total pembayaran
    private fun updateTotalPembayaran() {
        val totalPembayaran = productQuantities.entries.sumOf { (product, quantity) ->
            product.harga * quantity
        }
        txtTotalPembayaran.text = "Rp. $totalPembayaran"
        Log.d("AddTransaksiActivity", "Updated Total Pembayaran: Rp. $totalPembayaran")
    }
}
