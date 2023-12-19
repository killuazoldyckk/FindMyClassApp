package com.cc.findmyclasskotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListClassAdapter(private val listRooms: ArrayList<Room>): RecyclerView.Adapter<ListClassAdapter.ListViewHolder>() {

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: DashboardActivity) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_class, parent, false)

        return ListViewHolder(itemView)

    }

    override fun getItemCount(): Int = listRooms.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val currentItem = listRooms[position]

        holder.namaRuang.text = currentItem.namaRuang
        holder.matkul.text = currentItem.matkul
        // Mengubah format waktu sesuai keinginan
        val formattedTime = currentItem.jam?.let { formatClassTime(it) }
        holder.jam.text = formattedTime
        holder.hari.text = currentItem.hari
        holder.status.text = currentItem.status

    }

    // Fungsi untuk mengubah format waktu yang diambil dari Firebase
    private fun formatClassTime(rawTime: String): String {
        val timeSlots = rawTime.split(",") // Memisahkan waktu yang terpisah oleh koma
        val mergedSlots = mergeTimeSlots(timeSlots) // Menggabungkan waktu yang overlapping

        if (mergedSlots.isEmpty()) return ""

        val firstSlotParts = mergedSlots.first().split("-")
        val lastSlotParts = mergedSlots.last().split("-")

        val startTime = firstSlotParts.first()
        val endTime = lastSlotParts.last()

        return "$startTime - $endTime"
    }

    private fun mergeTimeSlots(timeSlots: List<String>): List<String> {
        val mergedSlots = mutableListOf<String>()
        if (timeSlots.isNotEmpty()) {
            val sortedSlots = timeSlots.sorted()

            var start = sortedSlots[0].substringBefore("-")
            var end = sortedSlots[0].substringAfter("-")

            for (i in 1 until sortedSlots.size) {
                val nextStart = sortedSlots[i].substringBefore("-")
                val nextEnd = sortedSlots[i].substringAfter("-")

                if (nextStart <= end) {
                    // Merge the time slots
                    end = if (nextEnd > end) nextEnd else end
                } else {
                    // Add the merged time slot and update start and end
                    mergedSlots.add("$start-$end")
                    start = nextStart
                    end = nextEnd
                }
            }
            // Add the last merged time slot
            mergedSlots.add("$start-$end")
        }
        return mergedSlots
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val namaRuang: TextView = itemView.findViewById(R.id.ruang_text)
        val matkul: TextView = itemView.findViewById(R.id.matkul_text)
        val hari: TextView = itemView.findViewById(R.id.hari_text)
        val jam: TextView = itemView.findViewById(R.id.waktu_text)
        val status: TextView = itemView.findViewById(R.id.status_text)
        private val bookingBtn: Button = itemView.findViewById(R.id.booking_btn)

        init {
            bookingBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    (itemView.context as? OnItemClickListener)?.onItemClick(position)
                }
            }
        }
    }
}