package com.example.easycash.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easycash.R
import com.example.easycash.models.Product

// Adapter untuk menampilkan daftar produk dalam transaksi dalam RecyclerView
class TransaksiAdapter(
    private val productList: List<Product>,
    private val onQuantityChange: (Product, Int) -> Unit
) : RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {

    // Peta untuk menyimpan kuantitas setiap produk
    private val productQuantities = mutableMapOf<Product, Int>()

    // ViewHolder untuk menyimpan referensi ke elemen UI dari item view
    inner class TransaksiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNamaTransaksi: TextView = itemView.findViewById(R.id.txtNamaTransaksi)
        val txtHargaTransaksi: TextView = itemView.findViewById(R.id.txtHargaTransaksi)
        val txtQty: TextView = itemView.findViewById(R.id.txtQty)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btnPlus)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btnMinus)

        // Mengikat data produk dan menambahkan onClickListener untuk tombol plus dan minus
        fun bind(product: Product) {
            txtNamaTransaksi.text = product.nama
            txtHargaTransaksi.text = "Rp. ${product.harga}"
            txtQty.text = productQuantities[product]?.toString() ?: "0"

            btnPlus.setOnClickListener {
                val newQuantity = (productQuantities[product] ?: 0) + 1
                productQuantities[product] = newQuantity
                txtQty.text = newQuantity.toString()
                onQuantityChange(product, newQuantity)
            }

            btnMinus.setOnClickListener {
                val newQuantity = (productQuantities[product] ?: 0) - 1
                if (newQuantity >= 0) {
                    productQuantities[product] = newQuantity
                    txtQty.text = newQuantity.toString()
                    onQuantityChange(product, newQuantity)
                }
            }
        }
    }

    // Membuat ViewHolder baru ketika RecyclerView membutuhkan satu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.purchase_item, parent, false)
        return TransaksiViewHolder(itemView)
    }

    // Mengikat data produk ke ViewHolder
    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    // Mendapatkan jumlah item dalam daftar produk
    override fun getItemCount(): Int = productList.size
}
