package com.example.easycash.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.example.easycash.R
import com.example.easycash.database.DatabaseHelper
import com.example.easycash.models.Payment
import com.example.easycash.models.Product
import com.example.easycash.models.Sale

class BayarActivity : AppCompatActivity() {

    // Deklarasi variabel untuk elemen UI dan DatabaseHelper
    private lateinit var db: DatabaseHelper
    private lateinit var icBack: ImageView
    private lateinit var txtTotalPembayaran: TextView
    private lateinit var txtJumlahPenerimaan: EditText
    private lateinit var txtKembalian: TextView
    private lateinit var btnSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bayar)

        // Inisialisasi elemen UI
        icBack = findViewById(R.id.ic_back)
        icBack.setOnClickListener { finish() }

        // Inisialisasi DatabaseHelper
        db = DatabaseHelper(this)

        // Ambil total pembayaran dan jumlah produk dari intent
        val totalPembayaran = intent.getDoubleExtra("TOTAL_PEMBAYARAN", 0.0)
        val productQuantities = intent.getSerializableExtra("PRODUCT_QUANTITIES") as HashMap<Product, Int>

        // Inisialisasi TextView dan EditText
        txtTotalPembayaran = findViewById(R.id.txtTotalTransaksi)
        txtJumlahPenerimaan = findViewById(R.id.txtJumlahPenerimaan)
        txtKembalian = findViewById(R.id.txtKembalian)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Set total pembayaran pada TextView
        txtTotalPembayaran.text = "Total: Rp. $totalPembayaran"

        // Set listener untuk EditText jumlah penerimaan untuk menghitung kembalian otomatis
        txtJumlahPenerimaan.addTextChangedListener {
            calculateChange(totalPembayaran)
        }

        // Set listener untuk tombol simpan
        btnSimpan.setOnClickListener { handleSimpanButton(totalPembayaran, productQuantities) }
    }

    // Fungsi untuk menghitung kembalian
    private fun calculateChange(totalPembayaran: Double) {
        val jumlahPenerimaanText = txtJumlahPenerimaan.text.toString()
        if (jumlahPenerimaanText.isNotEmpty()) {
            val jumlahPenerimaan = jumlahPenerimaanText.toDouble()
            val kembalian = jumlahPenerimaan - totalPembayaran
            txtKembalian.text = "Rp. $kembalian"
        } else {
            txtKembalian.text = "Rp. 0"
        }
    }

    // Fungsi untuk menangani tombol simpan
    private fun handleSimpanButton(totalPembayaran: Double, productQuantities: HashMap<Product, Int>) {
        // Validasi jumlah penerimaan
        val jumlahPenerimaanText = txtJumlahPenerimaan.text.toString()
        if (jumlahPenerimaanText.isEmpty()) {
            Toast.makeText(this, "Jumlah penerimaan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val jumlahPenerimaan = jumlahPenerimaanText.toDouble()

        if (jumlahPenerimaan < totalPembayaran) {
            Toast.makeText(this, "Jumlah penerimaan tidak boleh kurang dari total pembayaran", Toast.LENGTH_SHORT).show()
            return
        }

        // Lanjutkan dengan menyimpan transaksi
        simpanTransaksi(totalPembayaran, productQuantities, jumlahPenerimaan)
    }

    // Fungsi untuk menyimpan transaksi
    private fun simpanTransaksi(totalPembayaran: Double, productQuantities: HashMap<Product, Int>, jumlahPenerimaan: Double) {
        try {
            // Validasi nama pembeli
            val namaPembeli = findViewById<EditText>(R.id.txtNamaPembeli).text.toString()
            if (namaPembeli.isEmpty()) {
                Toast.makeText(this, "Nama pembeli tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }

            // Validasi metode pembayaran
            val radioGroupPaymentMethod: RadioGroup = findViewById(R.id.radioGroupPaymentMethod)
            val selectedPaymentMethodId = radioGroupPaymentMethod.checkedRadioButtonId
            if (selectedPaymentMethodId == -1) {
                Toast.makeText(this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show()
                return
            }

            val selectedPaymentMethod: RadioButton = findViewById(selectedPaymentMethodId)
            val metodePembayaran = selectedPaymentMethod.text.toString()
            val kembalian = jumlahPenerimaan - totalPembayaran

            // Buat objek payment
            val payment = Payment(
                nama_pembeli = namaPembeli,
                total_bayar = totalPembayaran,
                uang_bayar = jumlahPenerimaan,
                kembalian = kembalian,
                metode_pembayaran = metodePembayaran
            )

            // Simpan payment ke database dan ambil ID payment
            val paymentId = db.addPayment(payment)
            Log.d("BayarActivity", "Payment ID: $paymentId")

            // Konversi map produk dan jumlah ke dalam list Sale
            val saleList = productQuantities.map { (product, quantity) ->
                Sale(
                    id_pembayaran = paymentId.toInt(),
                    id_produk = product.id,
                    nama_produk = product.nama,
                    jumlah = quantity,
                    harga_satuan = product.harga
                )
            }

            // Simpan setiap item penjualan ke database
            saleList.forEach { sale -> db.addPurchaseDetail(sale) }

            Toast.makeText(this, "Pembayaran berhasil disimpan", Toast.LENGTH_SHORT).show()

            // Buat intent untuk memulai NotaActivity
            val intent = Intent(this, NotaActivity::class.java).apply {
                putExtra("PAYMENT", payment)
                putExtra("SALES_LIST", ArrayList(saleList))
            }
            startActivity(intent)
            finish() // Tutup BayarActivity
        } catch (e: Exception) {
            Log.e("BayarActivity", "Error saving transaction", e)
            Toast.makeText(this, "Terjadi kesalahan saat menyimpan transaksi", Toast.LENGTH_SHORT).show()
        }
    }
}