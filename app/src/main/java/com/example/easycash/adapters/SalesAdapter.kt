package com.example.easycash.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easycash.R
import com.example.easycash.models.Sale

// Adapter untuk menampilkan daftar penjualan dalam RecyclerView
class SalesAdapter(private val salesList: List<Sale>) : RecyclerView.Adapter<SalesAdapter.ViewHolder>() {
    // ViewHolder untuk menyimpan referensi ke elemen UI dari item view
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productIDTextView: TextView = view.findViewById(R.id.sale_product_id)
        val productNameTextView: TextView = view.findViewById(R.id.sale_product_name)
        val quantityTextView: TextView = view.findViewById(R.id.sale_quantity)
        val unitPriceTextView: TextView = view.findViewById(R.id.sale_unit_price)
        val totalPriceTextView: TextView = view.findViewById(R.id.sale_total_price)
    }

    // Membuat ViewHolder baru ketika RecyclerView membutuhkan satu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sale_list_item, parent, false)
        return ViewHolder(view)
    }

    // Mengikat data penjualan ke ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sale = salesList[position]
        holder.productIDTextView.text = "${position + 1}"
        holder.productNameTextView.text = sale.nama_produk
        holder.quantityTextView.text = "${sale.jumlah}"
        holder.unitPriceTextView.text = "${sale.harga_satuan}"
        holder.totalPriceTextView.text = "${sale.jumlah * sale.harga_satuan}"
    }

    // Mendapatkan jumlah item dalam daftar penjualan
    override fun getItemCount(): Int = salesList.size
}
