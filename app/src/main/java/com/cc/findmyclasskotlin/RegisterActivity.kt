package com.cc.findmyclasskotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.cc.findmyclasskotlin.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.backButton.setOnClickListener {
            val goToLogin = Intent(this, LoginActivity::class.java)
            startActivity(goToLogin)
        }

        binding.registerBtn.setOnClickListener {
            val nim = binding.editTextNIM.text.toString()
            val password = binding.editTextPassword.text.toString()
            val nama = binding.editTextNama.text.toString()

            // Buat email palsu dengan format nim@example.com untuk Firebase Authentication
            val email = "$nim@example.com"

            if (nama.isNotEmpty() && nim.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Menyimpan nama pengguna ke database saat registrasi berhasil
                            val currentUser = firebaseAuth.currentUser
                            val userId = currentUser?.uid // Mendapatkan UID pengguna yang baru dibuat
                            val namaUser = binding.editTextNama.text.toString()

                            // Mendapatkan referensi ke database Firebase Realtime
                            val database = FirebaseDatabase.getInstance()
                            val usersRef = database.getReference("users")

                            // Membuat entri data pengguna baru dengan nama pengguna
                            userId?.let {
                                val userRef = usersRef.child(it)
                                userRef.child("nama").setValue(namaUser)
                                    .addOnSuccessListener {
                                        // Lanjutkan ke aktivitas berikutnya setelah registrasi berhasil
                                        val goToLogin = Intent(this, LoginActivity::class.java)
                                        startActivity(goToLogin)
                                    }
                                    .addOnFailureListener { exception ->
                                        // Jika gagal menyimpan, tampilkan pesan kesalahan
                                        Toast.makeText(this@RegisterActivity, "Gagal menyimpan nama pengguna: ${exception.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
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