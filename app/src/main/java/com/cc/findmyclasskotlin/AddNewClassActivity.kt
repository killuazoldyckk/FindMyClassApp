package com.cc.findmyclasskotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.cc.findmyclasskotlin.databinding.ActivityAddNewClassBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddNewClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewClassBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var selectedMatkul: String? = null
    private lateinit var matkulAdapter: ArrayAdapter<String>
    private lateinit var dropdownPopup: PopupWindow
    private val selectedTimeSlots = mutableListOf<String>()
    private var popupWindow: PopupWindow? = null
    private var displayedTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("classroom")

        // Initialize the adapter as a property of the class
        matkulAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf())

        // Initialize the spinner
        val ruangSpinner = binding.dropdownRuang

        val exitBtn: ImageView = findViewById(R.id.exit_btn)
        exitBtn.setOnClickListener {
            val backToDashboard = Intent(this, DashboardActivity::class.java)
            startActivity(backToDashboard)
        }


        // Initialize the serachView
        val matkulSearch = binding.searchViewMatkul
        val autoCompleteTextView = matkulSearch.findViewById<AutoCompleteTextView>(R.id.search_src_text)

        // Use ViewTreeObserver to wait until the SearchView is measured
        matkulSearch.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                // Remove the listener to avoid multiple calls
                matkulSearch.viewTreeObserver.removeOnPreDrawListener(this)

                // Get the width of the SearchView
                val searchViewWidth = matkulSearch.width

                // Set the width of the AutoCompleteTextView to match the SearchView
                autoCompleteTextView.width = searchViewWidth

                return true
            }
        })

        autoCompleteTextView.setOnItemClickListener { adapterView: AdapterView<*>, view: View?, position: Int, id: Long ->
            // Handle item selection here, for example:
            val adapter = adapterView.adapter
            if (adapter is ArrayAdapter<*>) {
                selectedMatkul = adapter.getItem(position) as? String
                // Do something with the selectedMatkul
            }
        }

        // Set a query listener for handling user input in SearchView
        matkulSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the submitted query if needed
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle the text change in the SearchView
                matkulAdapter.filter.filter(newText)
                return true
            }
        })

        // Fetch unique "matkul" values from the database
        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val matkulSet = HashSet<String>()

                for (classSnapshot in snapshot.children) {
                    val matkul = classSnapshot.child("matkul").getValue(String::class.java)
                    if (matkul != null) {
                        matkulSet.add(matkul)
                    }
                }

                // Update the adapter data
                matkulAdapter.clear()
                matkulAdapter.addAll(matkulSet.toList())

                // Notify the adapter that the data set has changed
                matkulAdapter.notifyDataSetChanged()

                // Set the adapter to the AutoCompleteTextView
                autoCompleteTextView.setAdapter(matkulAdapter)


            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error if needed
                Toast.makeText(applicationContext, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
            }
        })

        // Read data from Firebase Realtime Database
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val ruangSet = HashSet<String>()

                for (classSnapshot in snapshot.children) {
                    val namaRuang = classSnapshot.child("namaRuang").getValue(String::class.java)
                    if (namaRuang != null) {
                        ruangSet.add(namaRuang)
                    }
                }

                // Convert HashSet to Array
                val ruangArray = ruangSet.toTypedArray()

                // Populate the spinner with dynamic array
                val ruangAdapter = ArrayAdapter(
                    this@AddNewClassActivity,
                    android.R.layout.simple_spinner_item,
                    ruangArray
                )
                ruangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                ruangSpinner.adapter = ruangAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Toast.makeText(
                    applicationContext,
                    "Failed to retrieve data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        binding.dropdownButton.setOnClickListener {
            // Toggle the visibility of the waktu dropdown
            toggleWaktuDropdown()
        }

        binding.bookingBtn.setOnClickListener {

            // Get user inputs
            val ruang = binding.dropdownRuang.selectedItem.toString()
            val hari = binding.dropdownHari.selectedItem.toString()
            // Concatenate the selected time slots into a string
            val jam = getCombinedTime(selectedTimeSlots)

            // Check if a matkul is selected
            if (selectedMatkul.isNullOrEmpty()) {
                // Show an error message or handle the case where matkul is not selected
                Toast.makeText(applicationContext, "Please select a matkul", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Query to check if data with the same namaRuang, hari, and jam exists
            val matchingQuery = database.orderByChild("namaRuang")
                .equalTo(ruang)

            matchingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var isClassBooked = false

                    for (snapshot in dataSnapshot.children) {
                        val room = snapshot.getValue(Room::class.java)
                        val roomKey = snapshot.key

                        if ((room != null) && (roomKey != null)) {
                            if (ruang == room.namaRuang && hari == room.hari && jam == room.jam) {
                                isClassBooked = true
                                break
                            }
                        }
                    }

                    if (isClassBooked) {
                        // Tampilkan pesan bahwa kelas sudah dibooking
                        // Misalnya, dengan Toast
                        Toast.makeText(this@AddNewClassActivity, "Kelas tersebut sudah dibooking", Toast.LENGTH_SHORT).show()
                    } else {
                        // Create a Room object
                        val room = Room(
                            namaRuang = ruang,
                            matkul = selectedMatkul,
                            hari = hari,
                            jam = jam,
                            status = "Terisi",
                            stambuk = "2021", // You can set the value as needed
                            kom = "B" // You can set the value as needed
                        )

                        // Push the data to the Realtime Database
                        val newClassRef = database.push()
                        newClassRef.setValue(room, object : DatabaseReference.CompletionListener {
                            override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                                if (error == null) {
                                    // Data was successfully written
                                    finish()
                                } else {
                                    // Handle the error
                                    val errorMessage = "Failed to save data: ${error.message}"
                                    Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error if needed
                }
            })
        }

    }

    private fun toggleWaktuDropdown() {
        // If the popup is showing, dismiss it; otherwise, show it
        if (popupWindow?.isShowing == true) {
            popupWindow?.dismiss()
        } else {
            showWaktuDropdown()
        }
    }

    private fun getCombinedTime(timeSlots: List<String>): String {
        val mergedSlots = mergeTimeSlots(timeSlots)
        val combinedTime = StringBuilder()

        if (mergedSlots.isEmpty()) return ""

        val firstSlotParts = mergedSlots.first().split("-")
        val lastSlotParts = mergedSlots.last().split("-")

        val startTime = firstSlotParts.first()
        val endTime = lastSlotParts.last()

        combinedTime.append(startTime)
        combinedTime.append(" - ")
        combinedTime.append(endTime)

        // Simpan waktu yang akan disimpan ke database tanpa spasi
        return combinedTime.toString()
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


    // Function to show the waktu dropdown
    private fun showWaktuDropdown() {
        val popupView = layoutInflater.inflate(R.layout.popup_waktu, null)
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true // This enables the popup to be dismissed when clicked outside
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
        popupWindow?.setOnDismissListener {
            // Update the displayed time with the merged time slots
            displayTime(selectedTimeSlots)
        }

        // Show the popup below the dropdownButton
        popupWindow?.showAsDropDown(binding.dropdownButton)
    }

    // Fungsi untuk menampilkan waktu yang diformat
    private fun displayTime(timeSlots: List<String>) {
        // Tampilkan waktu dalam format yang diinginkan pada antarmuka pengguna
        displayedTime = getCombinedTime(timeSlots)
        binding.textViewWaktu.text = displayedTime
    }
}