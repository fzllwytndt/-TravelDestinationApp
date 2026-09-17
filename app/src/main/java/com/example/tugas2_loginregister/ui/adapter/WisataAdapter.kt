package com.example.tugas2_loginregister.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas2_loginregister.R
import com.example.tugas2_loginregister.model.Wisata
import com.example.tugas2_loginregister.utils.Helper

/** Menampilkan daftar wisata yang diambil dari API pada Fragment Home. */
class WisataAdapter(
    private val saatDiklik: (Wisata) -> Unit
) : ListAdapter<Wisata, WisataAdapter.ViewHolder>(PEMBANDING) {

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wisata = getItem(position)

        holder.tvNama.text = wisata.namaWisata
        holder.tvDeskripsi.text = wisata.deskripsi

        Helper.muatGambar(holder.ivFoto, wisata.fotoUrl)

        holder.itemView.setOnClickListener {
            saatDiklik(wisata)
        }
    }

    companion object {

        /** Dipakai RecyclerView untuk mengetahui data mana saja yang berubah. */
        private val PEMBANDING = object : DiffUtil.ItemCallback<Wisata>() {

            override fun areItemsTheSame(lama: Wisata, baru: Wisata): Boolean {
                return lama.id == baru.id
            }

            override fun areContentsTheSame(lama: Wisata, baru: Wisata): Boolean {
                return lama == baru
            }
        }
    }
}
