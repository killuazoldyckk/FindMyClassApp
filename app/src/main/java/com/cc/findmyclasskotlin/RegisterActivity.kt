package com.cc.findmyclasskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cc.findmyclasskotlin.databinding.ActivityLoginBinding
import com.cc.findmyclasskotlin.databinding.ActivityMainBinding
import com.cc.findmyclasskotlin.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}