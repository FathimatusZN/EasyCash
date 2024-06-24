package com.example.easycash.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.easycash.R
import com.example.easycash.database.DatabaseHelper
import com.example.easycash.models.Product
import com.google.android.material.textfield.TextInputEditText

class AddProductActivity : AppCompatActivity() {
    // Deklarasi variabel untuk DatabaseHelper dan elemen UI
    private lateinit var db: DatabaseHelper
    private lateinit var txtFromNamaProduk: TextInputEditText
    private lateinit var txtFromHargaProduk: TextInputEditText
    private lateinit var txtFromStokProduk: TextInputEditText
    private lateinit var btnTambahProduk: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Inisialisasi DatabaseHelper
        db = DatabaseHelper(this)

        // Inisialisasi elemen UI
        txtFromNamaProduk = findViewById(R.id.txtFromNamaProduk)
        txtFromHargaProduk = findViewById(R.id.txtFromHargaProduk)
        txtFromStokProduk = findViewById(R.id.txtFromStokProduk)
        btnTambahProduk = findViewById(R.id.btnTambahProduk)

        // Inisialisasi dan set listener untuk tombol kembali
        val backIcon: ImageView = findViewById(R.id.ic_back)
        backIcon.setOnClickListener { handleBackButton() }

        // Set listener untuk tombol tambah produk
        btnTambahProduk.setOnClickListener { addProduct() }
    }

    // Fungsi untuk menambahkan produk ke database
    private fun addProduct() {
        val nama = txtFromNamaProduk.text.toString()
        val harga = txtFromHargaProduk.text.toString().toDoubleOrNull()
        val stok = txtFromStokProduk.text.toString().toIntOrNull()

        if (nama.isNotEmpty() && harga != null && stok != null) {
            val product = Product(nama = nama, harga = harga, stok = stok)
            val result = db.addProduct(product)

            if (result > 0) {
                Toast.makeText(this, "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish() // Kembali ke halaman sebelumnya
            } else {
                Toast.makeText(this, "Gagal menambahkan produk", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Isi semua data dengan benar", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk menangani tombol kembali
    private fun handleBackButton() {
        if (isFormDirty()) {
            showUnsavedChangesDialog()
        } else {
            navigateToProductList()
        }
    }

    // Fungsi untuk mengecek apakah form sudah diisi
    private fun isFormDirty(): Boolean {
        return txtFromNamaProduk.text.toString().isNotEmpty() ||
                txtFromHargaProduk.text.toString().isNotEmpty() ||
                txtFromStokProduk.text.toString().isNotEmpty()
    }

    // Menampilkan dialog konfirmasi jika terdapat perubahan yang belum disimpan
    private fun showUnsavedChangesDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi")
            .setMessage("Apakah Anda yakin kembali? Penambahan belum tersimpan.")
            .setPositiveButton("Ya, Buang Perubahan") { dialog, which -> navigateToProductList() }
            .setNegativeButton("Lanjut Mengedit", null)
            .show()
    }

    // Fungsi untuk navigasi kembali ke daftar produk
    private fun navigateToProductList() {
        val intent = Intent(this, ProductListActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Override fungsi onBackPressed untuk menangani tombol kembali
    override fun onBackPressed() {
        super.onBackPressed()
        handleBackButton()
    }
}
