package com.example.safeguardapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class PermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        val btn = findViewById<MaterialButton>(R.id.btnAllowPermissions)
        btn.setOnClickListener {
            requestPermissionsSafely()
        }
    }

    private fun requestPermissionsSafely() {

        val permissions = arrayOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE
        )

        val notGranted = ArrayList<String>()

        for (p in permissions) {
            val result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(p)
            }
        }

        if (notGranted.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                notGranted.toTypedArray(),
                101
            )
        } else {
            // All permissions already granted
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}