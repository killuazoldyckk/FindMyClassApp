package com.cc.findmyclasskotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.get
import com.cc.findmyclasskotlin.databinding.ActivityBookingClassBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BookingClassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingClassBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exitBtn: ImageView = findViewById(R.id.exit_btn)

        binding.bookingBtn.setOnClickListener {

            val namaRuang = binding.dropdownRuang.selectedItem.toString()
            val matkul = binding.editTextMatkul.text.toString()
            val hari = binding.dropdownHari.selectedItem.toString()
            val jam = binding.editTextWaktu.text.toString()
            val status = binding.dropdownStatus.selectedItem.toString()

            updateData(namaRuang,matkul, hari, jam, status)
        }

        exitBtn.setOnClickListener {
            val backToDashboard = Intent(this, DashboardActivity::class.java)
            startActivity(backToDashboard)
        }
    }

    private fun updateData(namaRuang: String, matkul: String, hari: String, jam: String, status: String) {

        database = FirebaseDatabase.getInstance().getReference("classroom")
        val booking = mapOf(
            "namaRuang" to namaRuang,
            "matkul" to matkul,
            "hari" to hari,
            "jam" to jam,
            "status" to status
        )

    }
}