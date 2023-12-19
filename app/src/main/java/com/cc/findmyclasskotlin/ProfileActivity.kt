package com.cc.findmyclasskotlin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cc.findmyclasskotlin.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var roomArrayList: ArrayList<Room>
    private lateinit var auth: FirebaseAuth
    private lateinit var user: User
    private lateinit var uid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        if (uid.isNotEmpty()) {

            getUserData()
        }

        binding.textView5.setOnClickListener {
            println("Clicked")

            // If user is not a komting, open a dialog window
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_apply_komting)

            // Initialize dialog views
            val applyTextView: TextView = dialog.findViewById(R.id.applyTextView)
            val yesButton: Button = dialog.findViewById(R.id.yesButton)
            val noButton: Button = dialog.findViewById(R.id.noButton)

            // Set the content of the TextView
            applyTextView.text = "Apply as komting?"

            // Set click listener for Yes button
            yesButton.setOnClickListener {
                // Update the user's status in the database
                val userUpdate = databaseReference.child(uid)
                userUpdate.child("isApply").setValue(true) // Add a new child "isApply" with the value true

                userUpdate.updateChildren(mapOf<String, Any>("isApply" to true))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@ProfileActivity, "You have applied as komting", Toast.LENGTH_SHORT).show()
                        } else {
                            val exception = task.exception
                            Toast.makeText(this@ProfileActivity, "Failed to apply: ${exception?.message}", Toast.LENGTH_SHORT).show()
                        }

                        // Dismiss the dialog
                        dialog.dismiss()
                    }
            }

            // Show the dialog
            dialog.show()
        }

        binding.homeBtn.setOnClickListener{
            val goToDashboardActivity = Intent(this,DashboardActivity::class.java)
            startActivity(goToDashboardActivity)
        }

        binding.historyBtn.setOnClickListener{
            val goToHistoryActivity = Intent(this,HistoryActivity::class.java)
            startActivity(goToHistoryActivity)
        }

        binding.btnLogout.setOnClickListener{
            // Sign out the user
            FirebaseAuth.getInstance().signOut()

            // Redirect to the login or authentication screen
            val goToLoginActivity = Intent(this, LoginActivity::class.java)
            goToLoginActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(goToLoginActivity)
            finish()
        }
    }

    private fun getUserData() {

        databaseReference.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                binding.namaLengkap.text = user.nama
                binding.textView3.text = user.nim
                binding.status.text = if (!user.isKomting!!) "Mahasiswa" else "Komting"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Failed to get user profile data", Toast.LENGTH_SHORT).show()
            }
        })

        // Add ChildEventListener for isKomting changes
        databaseReference.child(uid).child("isKomting").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isKomting = snapshot.getValue(Boolean::class.java) ?: false
                binding.status.text = if (!isKomting) "Mahasiswa" else "Komting"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Failed to get isKomting data", Toast.LENGTH_SHORT).show()
            }
        })
    }

}