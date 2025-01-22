package com.example.apliacaciondomotica

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RutinasActivity : AppCompatActivity() {

    private lateinit var btnCrearRutina: Button
    private lateinit var rutinaLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rutinas)

        btnCrearRutina = findViewById(R.id.btn_crear_rutina)
        rutinaLayout = findViewById(R.id.rutina_layout)

        btnCrearRutina.setOnClickListener {
            val intent = Intent(this, CrearRutinaActivity::class.java)
            startActivity(intent)
        }

        cargarRutinas()
    }

    private fun cargarRutinas() {
        // Obtener las rutinas de SharedPreferences
        val sharedPreferences = getSharedPreferences("Rutinas", Context.MODE_PRIVATE)

        for (key in sharedPreferences.all.keys) {
            // Solo mostramos las rutinas guardadas que tengan el formato "rutina_HH:mm"
            if (key.startsWith("rutina_")) {
                val dispositivo = sharedPreferences.getString(key, "No especificado") ?: "No especificado"
                val horaMinutos = key.substringAfter("rutina_")
                val textView = TextView(this)
                textView.text = "Rutina para $dispositivo a las $horaMinutos"
                textView.setTextColor(android.graphics.Color.WHITE)
                rutinaLayout.addView(textView)
            }
        }
    }
}
