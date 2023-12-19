package com.cc.findmyclasskotlin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cc.findmyclasskotlin.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        installSplashScreen()

        val firebaseAuth = FirebaseAuth.getInstance();

        binding.loginBtn.setOnClickListener {

            val nim = binding.editTextNIM.text.toString()
            val password = binding.editTextPassword.text.toString()

            val email = "$nim@example.com"
            if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val goToDashboard = Intent(this, DashboardActivity::class.java)
                        startActivity(goToDashboard)
                    } else {
                        // Login gagal, tampilkan pesan kesalahan
                        Toast.makeText(
                            this@LoginActivity,
                            "Akun tidak ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(this, "Semua fields harus diisi!", Toast.LENGTH_SHORT).show()
        }
        }


        binding.registBtn.setOnClickListener {
            val goToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(goToRegister)
        }

        fun validateInput(nim: String, password: String): Boolean {
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
}