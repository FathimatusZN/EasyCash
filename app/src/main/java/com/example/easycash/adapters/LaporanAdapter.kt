package com.example.easycash.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easycash.R
import com.example.easycash.activities.NotaActivity
import com.example.easycash.models.Payment
import java.text.SimpleDateFormat
import java.util.Locale

class LaporanAdapter(private val laporanList: List<Payment>) : RecyclerView.Adapter<LaporanAdapter.LaporanViewHolder>() {

    // Mendefinisikan format tanggal yang diinginkan
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanViewHolder {
        // Inflate layout item_laporan untuk setiap item dalam RecyclerView
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_laporan, parent, false)
        return LaporanViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LaporanViewHolder, position: Int) {
        // Dapatkan item saat ini dari daftar laporan
        val currentItem = laporanList[position]

        // Format tanggal transaksi
        val formattedDate = dateFormat.format(currentItem.tanggal)

        // Set nilai pada TextViews
        holder.txtTanggalTransaksi.text = formattedDate
        holder.txtNamaPembeli.text = currentItem.nama_pembeli
        holder.txtTransaksiTotal.text = "Rp. ${currentItem.total_bayar}"

        // Set onClickListener untuk itemView, membuka NotaActivity dengan membawa data yang diperlukan
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, NotaActivity::class.java).apply {
                putExtra("PAYMENT_ID", currentItem.id_pembayaran)
                putExtra("SOURCE", "LaporanActivity")
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = laporanList.size

    // ViewHolder untuk RecyclerView
    class LaporanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTanggalTransaksi: TextView = itemView.findViewById(R.id.txtTanggalTransaksi)
        val txtNamaPembeli: TextView = itemView.findViewById(R.id.txtNamaPembeli)
        val txtTransaksiTotal: TextView = itemView.findViewById(R.id.txtTransaksiTotal)
    }
}
