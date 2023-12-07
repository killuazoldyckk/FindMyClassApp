package com.cc.findmyclasskotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.cc.findmyclasskotlin.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mAuth = FirebaseAuth.getInstance();
        val nim = binding.editTextNIM.text.toString()
        val password = binding.editTextPassword.text.toString()
        val nama = binding.editTextNama.text.toString()


        binding.registerBtn.setOnClickListener {

            // Buat email palsu dengan format nim@example.com untuk Firebase Authentication
            val email = "$nim@example.com"
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registrasi berhasil
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Lakukan sesuatu dengan data pengguna yang baru terdaftar, misalnya, menyimpan nama
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nama)
                            .build();
                        if (user != null) {
                            user.updateProfile(profileUpdates);
                        }
                        // Lanjutkan ke aktivitas berikutnya setelah registrasi berhasil
                        val goToDashboardActivity = Intent(this, DashboardActivity::class.java)
                        startActivity(goToDashboardActivity);
                    } else {
                        // Registrasi gagal, tampilkan pesan kesalahan
                        Toast.makeText(
                            this@RegisterActivity,
                            "Akun tidak ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()                    }
                });
    }
}