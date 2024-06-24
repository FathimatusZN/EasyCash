package com.example.easycash.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easycash.R
import com.example.easycash.models.Product

// Adapter untuk menampilkan daftar produk dalam RecyclerView
class ProductAdapter(
    private val productList: List<Product>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    // Interface untuk menangani klik item
    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }

    // ViewHolder untuk menyimpan referensi ke elemen UI dari item view
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.product_name)
        val priceTextView: TextView = view.findViewById(R.id.product_price)
        val stockTextView: TextView = view.findViewById(R.id.product_stock)
    }

    // Membuat ViewHolder baru ketika RecyclerView membutuhkan satu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate layout item untuk produk
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_list_item, parent, false)
        return ViewHolder(view)
    }

    // Mengikat data produk ke ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Mendapatkan produk berdasarkan posisi
        val product = productList[position]
        // Mengatur teks untuk elemen UI di item view
        holder.nameTextView.text = product.nama
        holder.priceTextView.text = "Rp. ${product.harga}"
        holder.stockTextView.text = "${product.stok}"
        // Menambahkan OnClickListener untuk item view
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(product)
        }
    }

    // Mendapatkan jumlah item dalam daftar produk
    override fun getItemCount(): Int = productList.size
}
