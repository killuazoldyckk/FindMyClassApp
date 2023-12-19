package com.cc.findmyclasskotlin

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.Toast
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
    private val selectedTimeSlots = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exitBtn: ImageView = findViewById(R.id.exit_btn)
        val selectedRoom = intent.getParcelableExtra<Room>("SELECTED_ROOM")
        val isPreFilled = intent.getBooleanExtra("PRE_FILLED", false)

        // Mengisi field sesuai dengan data yang diterima
        if (isPreFilled && selectedRoom != null) {
            val ruangAdapter = binding.dropdownRuang.adapter
            if (ruangAdapter != null) {
                binding.dropdownRuang.setSelection(getIndex(binding.dropdownRuang, selectedRoom.namaRuang ?: ""))
            }

            binding.searchViewMatkul.setQuery(selectedRoom.matkul ?: "", false)

            val hariAdapter = binding.dropdownHari.adapter
            if (hariAdapter != null) {
                binding.dropdownHari.setSelection(getIndex(binding.dropdownHari, selectedRoom.hari ?: ""))
            }

            binding.textViewWaktu.text = selectedRoom.jam ?: ""

            val statusAdapter = binding.dropdownStatus.adapter
            if (statusAdapter != null) {
                binding.dropdownStatus.setSelection(getIndex(binding.dropdownStatus, selectedRoom.status ?: ""))
            }
        }

        binding.dropdownWaktu.setOnClickListener {
            // Show popup menu for selecting time slots
            showWaktuDropdown()
        }


        binding.bookingBtn.setOnClickListener {
            val selectedRoom = intent.getParcelableExtra<Room>("SELECTED_ROOM")

            val namaRuang = binding.dropdownRuang.selectedItem.toString()
            val matkul = binding.searchViewMatkul.query.toString()
            val hari = binding.dropdownHari.selectedItem.toString()
            val jam = binding.textViewWaktu.text.toString()
            val status = binding.dropdownStatus.selectedItem.toString()

            database = FirebaseDatabase.getInstance().getReference("classroom")

            // Flag to check if the class is booked by someone else
            var isClassBooked = false

            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val room = snapshot.getValue(Room::class.java)
                        val roomKey = snapshot.key

                        // Pastikan kelas yang dipilih untuk diperbarui tidak sama dengan kelas yang sudah ada
                        if ((room != null) && (roomKey != null) && (selectedRoom?.schedID != room.schedID)) {
                            if (namaRuang == room.namaRuang && hari == room.hari && jam == room.jam) {
                                isClassBooked = true
                                break
                            }
                        }
                    }

                    if (isClassBooked) {
                        Toast.makeText(this@BookingClassActivity, "Kelas sudah dibooking oleh komting lain", Toast.LENGTH_SHORT).show()
                    } else {
                        updateClassroom(selectedRoom, namaRuang, matkul, hari, jam, status)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors if needed
                }
            })
        }

        exitBtn.setOnClickListener {
            val backToDashboard = Intent(this, DashboardActivity::class.java)
            startActivity(backToDashboard)
        }
    }

    private fun updateClassroom(selectedRoom: Room?, namaRuang: String, matkul: String, hari: String, jam: String, status: String) {
        database = FirebaseDatabase.getInstance().getReference("classroom")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isClassFound = false

                for (snapshot in dataSnapshot.children) {
                    val room = snapshot.getValue(Room::class.java)
                    val roomKey = snapshot.key

                    // Pastikan kelas yang dipilih untuk diperbarui adalah yang sama dengan kelas yang ada di Firebase
                    if ((room != null) && (roomKey != null) && (selectedRoom?.schedID == room.schedID)) {
                        // Update data menggunakan kunci yang sesuai di Firebase
                        val updateData: MutableMap<String, Any> = HashMap()
                        updateData["namaRuang"] = namaRuang
                        updateData["matkul"] = matkul
                        updateData["hari"] = hari
                        updateData["jam"] = jam
                        updateData["status"] = status

                        snapshot.ref.updateChildren(updateData)
                        isClassFound = true
                        break
                    }
                }

                if (!isClassFound) {
                    Toast.makeText(this@BookingClassActivity, "Kelas tidak ditemukan", Toast.LENGTH_SHORT).show()
                } else {
                    val backToDashboard = Intent(this@BookingClassActivity, DashboardActivity::class.java)
                    startActivity(backToDashboard)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        })
    }

    private fun showWaktuDropdown() {
        val popupView = layoutInflater.inflate(R.layout.popup_waktu, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val waktuOptions = resources.getStringArray(R.array.waktu_options)

        val waktuCheckBoxGroup = popupView.findViewById<LinearLayout>(R.id.waktuCheckBoxGroup)

        // Inflate the waktu checkbox options dynamically
        for ((index, option) in waktuOptions.withIndex()) {
            val checkBox = CheckBox(this)
            checkBox.id = index
            checkBox.text = option
            waktuCheckBoxGroup.addView(checkBox)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                // Handle the checkbox selection
                if (isChecked) {
                    selectedTimeSlots.add(option)
                } else {
                    selectedTimeSlots.remove(option)
                }
            }
        }

        // Set the onDismissListener to update selectedWaktu when the popup is dismissed
        popupWindow.setOnDismissListener {
            // Update selectedWaktu if needed
        }

        // Show the popup below the dropdownButton
        popupWindow.showAsDropDown(binding.dropdownWaktu)
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
