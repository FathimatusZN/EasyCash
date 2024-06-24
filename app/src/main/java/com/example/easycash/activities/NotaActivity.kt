package com.example.easycash.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easycash.R
import com.example.easycash.adapters.SalesAdapter
import com.example.easycash.database.DatabaseHelper
import com.example.easycash.models.Payment
import com.example.easycash.models.Sale
import java.text.SimpleDateFormat
import java.util.Locale

class NotaActivity : AppCompatActivity() {

    // Deklarasi variabel untuk elemen UI dan DatabaseHelper
    private lateinit var db: DatabaseHelper
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nota)

        // Inisialisasi database helper
        db = DatabaseHelper(this)

        // Mengambil data dari intent
        val paymentId = intent.getIntExtra("PAYMENT_ID", -1)
        val source = intent.getStringExtra("SOURCE")

        val payment: Payment?
        val salesList: List<Sale>

        // Jika diakses dari LaporanActivity : Mengambil data dari database berdasarkan id_pembayaran
        if (source == "LaporanActivity") {
            payment = db.getPaymentById(paymentId)
            salesList = db.getSalesByPaymentId(paymentId)
        } else {
            // Jika diakses dari BayarActivity : Mengambil data dari intent pada halaman Transaksi dan halaman Bayar (data payment dan salesList
            payment = intent.getSerializableExtra("PAYMENT") as Payment
            salesList = intent.getSerializableExtra("SALES_LIST") as ArrayList<Sale>
        }

        val formattedDate = dateFormat.format(payment?.tanggal)

        // Set data to views
        findViewById<TextView>(R.id.textViewBuyerName).text = "Nama Pembeli              : ${payment?.nama_pembeli}"
        findViewById<TextView>(R.id.textViewOrderTime).text = "Waktu Pemesanan       : $formattedDate"
        findViewById<TextView>(R.id.textViewPaymentMethod).text = "Metode Pembayaran   : ${payment?.metode_pembayaran}"

        // Set nilai kembalian ke TextView
        findViewById<TextView>(R.id.textViewTotalOrderValue).text = "Rp. ${payment?.total_bayar}"
        findViewById<TextView>(R.id.textViewAmountPaidValue).text = "Rp. ${payment?.uang_bayar}"
        findViewById<TextView>(R.id.textViewChangeValue).text = "Rp. ${payment?.kembalian}"
        // Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewOrderDetails)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SalesAdapter(salesList)

        // Set up finish button
        findViewById<Button>(R.id.buttonFinish).setOnClickListener {
            if (source == "LaporanActivity") {
                // Jika Nota dibuka dari Laporan Activity maka finish akan mengarahkan kembali ke halaman Laporan
                finish()
            } else {
                // Jika Nota dibuka dari Bayar Activity maka finish akan mengarahkan kembali ke halaman Tambah Transaksi
                val intent = Intent(this, AddTransaksiActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
}
