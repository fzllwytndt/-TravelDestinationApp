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
import com.example.tugas2_loginregister.data.local.room.FavoriteWisata
import com.example.tugas2_loginregister.utils.Helper

/** Menampilkan daftar wisata favorit yang diambil dari Room Database. */
class FavoriteWisataAdapter(
    private val saatDiklik: (FavoriteWisata) -> Unit
) : ListAdapter<FavoriteWisata, FavoriteWisataAdapter.ViewHolder>(PEMBANDING) {

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
        val favorit = getItem(position)

        holder.tvNama.text = favorit.namaWisata
        holder.tvDeskripsi.text = favorit.lokasi

        Helper.muatGambar(holder.ivFoto, favorit.fotoUrl)

        holder.itemView.setOnClickListener {
            saatDiklik(favorit)
        }
    }

    companion object {

        private val PEMBANDING = object : DiffUtil.ItemCallback<FavoriteWisata>() {

            override fun areItemsTheSame(lama: FavoriteWisata, baru: FavoriteWisata): Boolean {
                return lama.id == baru.id
            }

            override fun areContentsTheSame(lama: FavoriteWisata, baru: FavoriteWisata): Boolean {
                return lama == baru
            }
        }
    }
}
