package com.cc.findmyclasskotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.get
import com.cc.findmyclasskotlin.databinding.ActivityBookingClassBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookingClassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingClassBinding
    // private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exitBtn: ImageView = findViewById(R.id.exit_btn)
        val selectedRoom = intent.getParcelableExtra<Room>("SELECTED_ROOM")

        binding.bookingBtn.setOnClickListener {

            val namaRuang = binding.dropdownRuang.selectedItem.toString()
            val matkul = binding.editTextMatkul.text.toString()
            val hari = binding.dropdownHari.selectedItem.toString()
            val jam = binding.editTextWaktu.text.toString()
            val status = binding.dropdownStatus.selectedItem.toString()

            val database = FirebaseDatabase.getInstance()
            val classroomReference = database.getReference("classroom")

            val query = classroomReference.orderByChild("hari").equalTo(hari)
                .orderByChild("jam").equalTo(jam)

            query.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val uniqueId = snapshot.key

                        uniqueId?.let {
                            classroomReference.child(it).child("namaRuang").setValue(namaRuang)
                            classroomReference.child(it).child("matkul").setValue(matkul)
                            classroomReference.child(it).child("status").setValue(status)
                        }
                    }
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


}