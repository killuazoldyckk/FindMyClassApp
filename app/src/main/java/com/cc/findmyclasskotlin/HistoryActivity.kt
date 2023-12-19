package com.cc.findmyclasskotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cc.findmyclasskotlin.databinding.ActivityHistoryBinding
import com.google.firebase.database.DatabaseReference

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeBtn.setOnClickListener{
            val goToDashboardActivity = Intent(this,DashboardActivity::class.java)
            startActivity(goToDashboardActivity)
        }

        binding.profileBtn.setOnClickListener{
            val gotToProfieActivity = Intent(this,ProfileActivity::class.java)
            startActivity(gotToProfieActivity)
        }
    }


}