package com.example.apliacaciondomotica

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tvStatus: TextView
    private lateinit var volumeStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tv_status)
        volumeStatus = findViewById(R.id.volume_status)

        // Recuperar valores desde SharedPreferences
        updateTextViewsFromPreferences()

        // Botón "TV"
        val btnTv = findViewById<Button>(R.id.btn_tv)
        btnTv.setOnClickListener {
            val intent = Intent(this, TvControlActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_TV_CONTROL)
        }
    }

    override fun onResume() {
        super.onResume()
        // Actualizar los valores al volver a esta actividad
        updateTextViewsFromPreferences()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_TV_CONTROL && resultCode == RESULT_OK && data != null) {
            // Obtener los datos del Intent
            val isTvOn = data.getBooleanExtra("tvState", false)
            val volumeLevel = data.getIntExtra("volumeLevel", 50)

            // Actualizar los TextView
            tvStatus.text = if (isTvOn) "Encendido" else "Apagado"
            volumeStatus.text = "Volumen: $volumeLevel"

            // Si la TV está apagada, hacer invisible el TextView del volumen
            volumeStatus.visibility = if (isTvOn) View.VISIBLE else View.GONE
        }
    }

    private fun updateTextViewsFromPreferences() {
        // Recuperar valores desde SharedPreferences
        val sharedPreferences = getSharedPreferences("TvSettings", Context.MODE_PRIVATE)
        val isTvOn = sharedPreferences.getBoolean("tvState", false)
        val volumeLevel = sharedPreferences.getInt("volumeLevel", 50)

        // Actualizar los TextView
        tvStatus.text = if (isTvOn) "Encendido" else "Apagado"
        volumeStatus.text = "Volumen: $volumeLevel"

        // Si la TV está apagada, hacer invisible el TextView del volumen
        volumeStatus.visibility = if (isTvOn) View.VISIBLE else View.GONE
    }

    companion object {
        private const val REQUEST_CODE_TV_CONTROL = 1
    }
}
