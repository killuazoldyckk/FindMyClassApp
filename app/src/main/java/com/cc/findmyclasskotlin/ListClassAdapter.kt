package com.cc.findmyclasskotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListClassAdapter(private val listRooms: ArrayList<Room>): RecyclerView.Adapter<ListClassAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_class, parent, false)

        return ListViewHolder(itemView)
    }

    override fun getItemCount(): Int = listRooms.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val currentItem = listRooms[position]

        holder.namaRuang.text = currentItem.namaRuang
        holder.matkul.text = currentItem.matkul
        holder.hari.text = currentItem.hari
        holder.jam.text = currentItem.jam
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val namaRuang: TextView = itemView.findViewById(R.id.ruang_title)
        val matkul: TextView = itemView.findViewById(R.id.matkul_text)
        val hari: TextView = itemView.findViewById(R.id.hari_text)
        val jam: TextView = itemView.findViewById(R.id.waktu_text)
    }
}