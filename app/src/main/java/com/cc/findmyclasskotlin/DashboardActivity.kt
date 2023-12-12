package com.cc.findmyclasskotlin

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cc.findmyclasskotlin.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var classRecyclerView: RecyclerView
    private lateinit var roomArrayList: ArrayList<Room>
    private lateinit var auth: FirebaseAuth
    private lateinit var user: User
    private lateinit var uid: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        if (uid.isNotEmpty()) {

            getUserData()
        }


        classRecyclerView = findViewById(R.id.class_list)
        classRecyclerView.layoutManager = LinearLayoutManager(this)
        classRecyclerView.setHasFixedSize(true)

        roomArrayList = arrayListOf<Room>()
        getClassData()
    }

    private fun getUserData() {

        databaseReference.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                binding.namaLengkap.text = user.nama
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DashboardActivity, "Failed to get user profile data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getClassData() {

        dbref = FirebaseDatabase.getInstance().getReference("ruangan")

        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (roomSnapshot in snapshot.children) {

                        val room = roomSnapshot.getValue(Room::class.java)
                        roomArrayList.add(room!!)
                    }

                    classRecyclerView.adapter = ListClassAdapter(roomArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}