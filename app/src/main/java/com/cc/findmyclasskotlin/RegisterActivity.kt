package com.cc.findmyclasskotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.cc.findmyclasskotlin.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerBtn.setOnClickListener {
            val nim = binding.editTextNIM.text.toString()
            val password = binding.editTextPassword.text.toString()
            val nama = binding.editTextNama.text.toString()

            // Buat email palsu dengan format nim@example.com untuk Firebase Authentication
            val email = "$nim@example.com"

            if (nama.isNotEmpty() && nim.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            // Lanjutkan ke aktivitas berikutnya setelah registrasi berhasil
                            val goToLogin = Intent(this, LoginActivity::class.java)
                            startActivity(goToLogin)
                        } else {
                            // Registrasi gagal, tampilkan pesan kesalahan
                            Toast.makeText(this@RegisterActivity, "Registrasi gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Semua fields harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}