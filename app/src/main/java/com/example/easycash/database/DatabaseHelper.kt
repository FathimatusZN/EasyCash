package com.example.easycash.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.easycash.models.Product
import com.example.easycash.models.Payment
import com.example.easycash.models.Sale
import java.util.Date

class DatabaseHelper(context: Context) {
    private var dbHelper: AppDatabase = AppDatabase(context)

    fun getReadableDatabase(): SQLiteDatabase {
        return dbHelper.readableDatabase
    }

    fun getWritableDatabase(): SQLiteDatabase {
        return dbHelper.writableDatabase
    }

    // Fungsi untuk menambahkan produk
    fun addProduct(product: Product): Long {
        val db = getWritableDatabase()
        val values = ContentValues()
        values.put("nama", product.nama)
        values.put("harga", product.harga)
        values.put("stok", product.stok)
        return db.insert("Produk", null, values)
    }

    // Fungsi untuk mengedit produk
    fun updateProduct(product: Product): Int {
        val db = getWritableDatabase()
        val values = ContentValues().apply {
            put("nama", product.nama)
            put("harga", product.harga)
            put("stok", product.stok)
        }
        return db.update("Produk", values, "id = ?", arrayOf(product.id.toString()))
    }

    // Fungsi untuk menghapus produk
    fun deleteProduct(productId: Int): Int {
        val db = getWritableDatabase()
        return db.delete("Produk", "id = ?", arrayOf(productId.toString()))
    }

    // Fungsi untuk mengambil semua produk
    fun getAllProducts(): List<Product> {
        val db = getReadableDatabase()
        val cursor = db.rawQuery("SELECT * FROM Produk", null)
        val products = mutableListOf<Product>()

        val idIndex = cursor.getColumnIndexOrThrow("id")
        val nameIndex = cursor.getColumnIndexOrThrow("nama")
        val priceIndex = cursor.getColumnIndexOrThrow("harga")
        val stockIndex = cursor.getColumnIndexOrThrow("stok")

        if (cursor.moveToFirst()) {
            do {
                products.add(
                    Product(
                        id = cursor.getInt(idIndex),
                        nama = cursor.getString(nameIndex),
                        harga = cursor.getDouble(priceIndex),
                        stok = cursor.getInt(stockIndex)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return products
    }

    // Fungsi untuk menambahkan pembayaran
    fun addPayment(payment: Payment): Long {
        val db = getWritableDatabase()
        val values = ContentValues()
        values.put("total_bayar", payment.total_bayar)
        values.put("nama_pembeli", payment.nama_pembeli)
        values.put("metode_pembayaran", payment.metode_pembayaran)
        values.put("tanggal", payment.tanggal.time) // Menyimpan tanggal sebagai timestamp
        values.put("uang_bayar", payment.uang_bayar)
        values.put("kembalian", payment.kembalian)
        return db.insert("Pembayaran", null, values)
    }

    // Fungsi untuk menambahkan detail pembelian
    fun addPurchaseDetail(sale: Sale): Long {
        val db = getWritableDatabase()
        val values = ContentValues()
        values.put("id_pembayaran", sale.id_pembayaran)
        values.put("id_produk", sale.id_produk)
        values.put("nama_produk", sale.nama_produk)
        values.put("jumlah", sale.jumlah)
        values.put("harga_satuan", sale.harga_satuan)
        return db.insert("DetilPembelian", null, values)
    }

    // Fungsi untuk mengambil semua laporan
    fun getAllLaporan(): List<Payment> {
        val db = getReadableDatabase()
        val cursor = db.rawQuery("SELECT * FROM Pembayaran", null)
        val laporan = mutableListOf<Payment>()

        val paymentIdIndex = cursor.getColumnIndexOrThrow("id_pembayaran")
        val dateIndex = cursor.getColumnIndexOrThrow("tanggal")
        val namaIndex = cursor.getColumnIndexOrThrow("nama_pembeli")
        val totalIndex = cursor.getColumnIndexOrThrow("total_bayar")
        val bayarIndex = cursor.getColumnIndexOrThrow("uang_bayar")
        val kembalianIndex = cursor.getColumnIndexOrThrow("kembalian")
        val metodeIndex = cursor.getColumnIndexOrThrow("metode_pembayaran")

        if (cursor.moveToFirst()) {
            do {
                laporan.add(
                    Payment(
                        id_pembayaran = cursor.getInt(paymentIdIndex),
                        tanggal = Date(cursor.getLong(dateIndex)),
                        nama_pembeli = cursor.getString(namaIndex),
                        total_bayar = cursor.getDouble(totalIndex),
                        uang_bayar = cursor.getDouble(bayarIndex),
                        kembalian = cursor.getDouble(kembalianIndex),
                        metode_pembayaran = cursor.getString(metodeIndex)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return laporan
    }

    // Fungsi untuk mencari riwayat pembayaran/transaksi berdasarkan id_pembayaran
    fun getPaymentById(paymentId: Int): Payment? {
        val db = getReadableDatabase()
        var payment: Payment? = null
        val cursor = db.rawQuery("SELECT * FROM Pembayaran WHERE id_pembayaran = ?", arrayOf(paymentId.toString()))
        if (cursor.moveToFirst()) {
            payment = Payment(
                id_pembayaran = cursor.getInt(cursor.getColumnIndexOrThrow("id_pembayaran")),
                nama_pembeli = cursor.getString(cursor.getColumnIndexOrThrow("nama_pembeli")),
                tanggal = Date(cursor.getLong(cursor.getColumnIndexOrThrow("tanggal"))),
                metode_pembayaran = cursor.getString(cursor.getColumnIndexOrThrow("metode_pembayaran")),
                total_bayar = cursor.getDouble(cursor.getColumnIndexOrThrow("total_bayar")),
                uang_bayar = cursor.getDouble(cursor.getColumnIndexOrThrow("uang_bayar")),
                kembalian = cursor.getDouble(cursor.getColumnIndexOrThrow("kembalian"))
            )
        }
        cursor.close()
        return payment
    }

    // Fungsi untuk mencari detail riwayat pembayaran/transaksi berdasarkan id_pembayaran
    fun getSalesByPaymentId(paymentId: Int): List<Sale> {
        val salesList = mutableListOf<Sale>()
        val db = getReadableDatabase()
        val cursor = db.rawQuery("SELECT * FROM DetilPembelian WHERE id_pembayaran = ?", arrayOf(paymentId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val sale = Sale(
                    id_detil = cursor.getInt(cursor.getColumnIndexOrThrow("id_detil")),
                    id_pembayaran = cursor.getInt(cursor.getColumnIndexOrThrow("id_pembayaran")),
                    id_produk = cursor.getInt(cursor.getColumnIndexOrThrow("id_produk")),
                    nama_produk = cursor.getString(cursor.getColumnIndexOrThrow("nama_produk")),
                    jumlah = cursor.getInt(cursor.getColumnIndexOrThrow("jumlah")),
                    harga_satuan = cursor.getDouble(cursor.getColumnIndexOrThrow("harga_satuan"))
                )
                salesList.add(sale)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return salesList
    }

}
