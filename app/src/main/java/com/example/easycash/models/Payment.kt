package com.example.easycash.models

import java.io.Serializable
import java.util.Date

data class Payment(
    var id_pembayaran: Int = 0,
    var tanggal: Date = Date(),
    var nama_pembeli: String,
    var total_bayar: Double,
    var uang_bayar: Double,
    var kembalian: Double,
    var metode_pembayaran: String
) : Serializable
