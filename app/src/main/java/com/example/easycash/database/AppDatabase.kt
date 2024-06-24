package com.example.easycash.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "easycash.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Produk (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nama TEXT NOT NULL,
                harga INTEGER NOT NULL,
                stok INTEGER NOT NULL
            )
        """)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Pembayaran (
                id_pembayaran INTEGER PRIMARY KEY AUTOINCREMENT,
                tanggal TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                nama_pembeli TEXT NOT NULL,
                total_bayar INTEGER NOT NULL,
                uang_bayar INTEGER NOT NULL,
                kembalian INTEGER NOT NULL,
                metode_pembayaran TEXT NOT NULL
            )
        """)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS DetilPembelian (
                id_detil INTEGER PRIMARY KEY AUTOINCREMENT,
                id_pembayaran INTEGER NOT NULL,
                id_produk INTEGER NOT NULL,
                nama_produk TEXT NOT NULL,
                jumlah INTEGER NOT NULL,
                harga_satuan INTEGER NOT NULL,
                FOREIGN KEY (id_pembayaran) REFERENCES Pembayaran(id_pembayaran),
                FOREIGN KEY (id_produk) REFERENCES Produk(id)
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Implement schema changes and data message here when upgrading
    }
}
