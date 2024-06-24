package com.example.easycash.models
import java.io.Serializable

data class Product(
    var id: Int = 0,
    var nama: String,
    var harga: Double,
    var stok: Int
) : Serializable
