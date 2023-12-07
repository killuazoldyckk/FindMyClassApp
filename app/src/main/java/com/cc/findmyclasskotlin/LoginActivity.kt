package com.cc.findmyclasskotlin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cc.findmyclasskotlin.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mAuth = FirebaseAuth.getInstance();
        val nim = binding.editTextNIM.text.toString()
        val password = binding.editTextPassword.text.toString()

        binding.loginBtn.setOnClickListener{

            val email = "$nim@example.com"
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> run {

            if (task.isSuccessful()) {
                Intent goToDashboard = new Intent(this, Dashboard.class);
                startActivity(goToDashboard);
            } else {
                // Login gagal, tampilkan pesan kesalahan
                Toast.makeText(LoginActivity.this, "Akun tidak ditemukan", Toast.LENGTH_SHORT)
                    .show();
            }
        };
        }

        binding.registBtn.setOnClickListener{
            val goToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(goToRegister)
        }
    }

    fun validateInput(nim:String,password:String): Boolean {
        if (nim.isEmpty()) {
            binding.editTextNIM.error = "Tidak boleh kosong"
            return false
        }
        if (password.isEmpty()) {
            binding.editTextPassword.error = "Tidak boleh kosong"
            return false
        }
        return true
    }
}