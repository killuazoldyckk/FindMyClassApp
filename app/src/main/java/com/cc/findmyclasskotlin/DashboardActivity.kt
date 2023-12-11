package com.cc.findmyclasskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardActivity : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var classRecyclerView: RecyclerView
    private lateinit var roomArrayList: ArrayList<Room>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        classRecyclerView = findViewById(R.id.class_list)
        classRecyclerView.layoutManager = LinearLayoutManager(this)
        classRecyclerView.setHasFixedSize(true)

        roomArrayList = arrayListOf<Room>()
        getClassData()
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