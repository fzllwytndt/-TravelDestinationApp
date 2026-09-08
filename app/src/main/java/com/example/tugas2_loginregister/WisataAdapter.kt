package com.example.tugas2_loginregister

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class WisataAdapter(private val daftar: List<Wisata>) :
    RecyclerView.Adapter<WisataAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivFoto: ImageView = view.findViewById(R.id.ivFoto)
        val tvNama: TextView = view.findViewById(R.id.tvNama)
        val tvDeskripsi: TextView = view.findViewById(R.id.tvDeskripsi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wisata, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return daftar.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wisata = daftar[position]

        holder.tvNama.text = wisata.namaWisata
        holder.tvDeskripsi.text = wisata.deskripsi

        Glide.with(holder.itemView.context)
            .load(wisata.fotoUrl)
            .into(holder.ivFoto)
    }
}
