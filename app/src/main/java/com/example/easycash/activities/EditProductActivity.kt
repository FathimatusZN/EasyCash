package com.example.easycash.activities

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.easycash.R
import com.example.easycash.database.DatabaseHelper
import com.example.easycash.models.Product
import com.google.android.material.textfield.TextInputEditText

class EditProductActivity : AppCompatActivity() {
    // Deklarasi variabel untuk elemen UI dan DatabaseHelper
    private lateinit var txtEditNamaProduk: TextInputEditText
    private lateinit var txtEditHargaProduk: TextInputEditText
    private lateinit var txtEditStokProduk: TextInputEditText
    private lateinit var btnEditProduk: Button
    private lateinit var btnHapusProduk: Button
    private lateinit var icBack: ImageView

    private lateinit var dbHelper: DatabaseHelper
    private var productId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        // Inisialisasi DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Inisialisasi elemen UI
        txtEditNamaProduk = findViewById(R.id.txtEditNamaProduk)
        txtEditHargaProduk = findViewById(R.id.txtEditHargaProduk)
        txtEditStokProduk = findViewById(R.id.txtEditStokProduk)
        btnEditProduk = findViewById(R.id.btnEditProduk)
        btnHapusProduk = findViewById(R.id.btnHapusProduk)
        icBack = findViewById(R.id.ic_back)

        // Ambil data produk dari intent
        productId = intent.getIntExtra("PRODUCT_ID", -1)
        val productName = intent.getStringExtra("PRODUCT_NAME")
        val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0)
        val productStock = intent.getIntExtra("PRODUCT_STOCK", 0)

        // Set data produk ke elemen UI
        txtEditNamaProduk.setText(productName)
        txtEditHargaProduk.setText(productPrice.toString())
        txtEditStokProduk.setText(productStock.toString())

        // Listener untuk tombol edit produk
        btnEditProduk.setOnClickListener {
            // Validasi input
            if (validateInput()) {
                val updatedProduct = Product(
                    id = productId,
                    nama = txtEditNamaProduk.text.toString(),
                    harga = txtEditHargaProduk.text.toString().toDouble(),
                    stok = txtEditStokProduk.text.toString().toInt()
                )

                // Update produk di database
                val result = dbHelper.updateProduct(updatedProduct)
                if (result > 0) {
                    setResult(Activity.RESULT_OK)
                    Toast.makeText(this, "Produk berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal memperbarui produk", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Listener untuk tombol hapus produk
        btnHapusProduk.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Produk")
                .setMessage("Anda yakin ingin menghapus produk ini?")
                .setPositiveButton("Ya") { dialog, _ ->
                    val result = dbHelper.deleteProduct(productId)
                    if (result > 0) {
                        setResult(Activity.RESULT_OK)
                        Toast.makeText(this, "Produk berhasil dihapus", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal menghapus produk", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        // Listener untuk tombol kembali
        icBack.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(): Boolean {
        // Validasi nama produk tidak boleh kosong
        if (txtEditNamaProduk.text.isNullOrEmpty()) {
            Toast.makeText(this, "Nama produk tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validasi harga produk tidak boleh kosong dan harus berupa angka
        if (txtEditHargaProduk.text.isNullOrEmpty()) {
            Toast.makeText(this, "Harga produk tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        val harga: Double? = txtEditHargaProduk.text.toString().toDoubleOrNull()
        if (harga == null || harga < 0) {
            Toast.makeText(this, "Harga produk harus berupa angka positif", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validasi stok produk tidak boleh kosong dan harus berupa angka
        if (txtEditStokProduk.text.isNullOrEmpty()) {
            Toast.makeText(this, "Stok produk tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        val stok: Int? = txtEditStokProduk.text.toString().toIntOrNull()
        if (stok == null || stok < 0) {
            Toast.makeText(this, "Stok produk harus berupa angka positif", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}