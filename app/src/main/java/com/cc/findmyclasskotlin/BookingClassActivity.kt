package com.cc.findmyclasskotlin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.cc.findmyclasskotlin.databinding.ActivityBookingClassBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookingClassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingClassBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exitBtn: ImageView = findViewById(R.id.exit_btn)
        val selectedRoom = intent.getParcelableExtra<Room>("SELECTED_ROOM")
        val isPreFilled = intent.getBooleanExtra("PRE_FILLED", false)

        // Mengisi field sesuai dengan data yang diterima
        if (isPreFilled && selectedRoom != null) {
            binding.dropdownRuang.setSelection(getIndex(binding.dropdownRuang, selectedRoom.namaRuang
                ?: ""))
            binding.editTextMatkul.setText(selectedRoom.matkul ?: "")
            binding.dropdownHari.setSelection(getIndex(binding.dropdownHari, selectedRoom.hari ?: ""))
            binding.editTextWaktu.setText(selectedRoom.jam ?: "")
            binding.dropdownStatus.setSelection(getIndex(binding.dropdownStatus, selectedRoom.status
                ?: ""))

        }

        binding.bookingBtn.setOnClickListener {
            val namaRuang = binding.dropdownRuang.selectedItem.toString()
            val matkul = binding.editTextMatkul.text.toString()
            val hari = binding.dropdownHari.selectedItem.toString()
            val jam = binding.editTextWaktu.text.toString()
            val status = binding.dropdownStatus.selectedItem.toString()

            // Dapatkan referensi database
            val database = FirebaseDatabase.getInstance().getReference("classroom")

            // Buat query berdasarkan hari dan jam
            val query = database.orderByChild("hari").equalTo(hari).orderByChild("jam").equalTo(jam)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val uniqueId = snapshot.key

                        uniqueId?.let {
                            // Update data di Firebase
                            database.child(it).child("namaRuang").setValue(namaRuang)
                            database.child(it).child("matkul").setValue(matkul)
                            database.child(it).child("hari").setValue(hari)
                            database.child(it).child("jam").setValue(jam)
                            database.child(it).child("status").setValue(status)
                        }
                    }
                    // Setelah update, kembali ke DashboardActivity
                    val backToDashboard = Intent(this@BookingClassActivity, DashboardActivity::class.java)
                    startActivity(backToDashboard)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Tangani kesalahan jika diperlukan
                }
            })
        }

        exitBtn.setOnClickListener {
            val backToDashboard = Intent(this, DashboardActivity::class.java)
            startActivity(backToDashboard)
        }
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString() == myString) {
                return i
            }
        }
        return 0
    }

}