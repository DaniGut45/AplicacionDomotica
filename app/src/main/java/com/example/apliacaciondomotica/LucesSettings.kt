package com.example.apliacaciondomotica

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LucesSettings : AppCompatActivity() {
    private lateinit var switchLuces: Switch
    private lateinit var buttonBorrar: Button
    private lateinit var buttonActualizar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_luces)

        switchLuces = findViewById(R.id.switch_luces)
        buttonBorrar = findViewById(R.id.buttonBorrar)
        buttonActualizar = findViewById(R.id.buttonActualizar)

        // Recuperar el estado guardado del switch
        val sharedPreferences = getSharedPreferences("LucesSettings", Context.MODE_PRIVATE)
        val isLucesOn = sharedPreferences.getBoolean("lucesState", false)
        switchLuces.isChecked = isLucesOn

        // Cambiar el estado del switch
        switchLuces.setOnCheckedChangeListener { _, isChecked ->
            // Guardar el estado del switch en SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putBoolean("lucesState", isChecked)
            editor.apply()
        }

        buttonActualizar.setOnClickListener {
            Toast.makeText(this, "Actualizando Firmware...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        // Guardar el estado del switch al salir de la actividad
        val editor = getSharedPreferences("LucesSettings", Context.MODE_PRIVATE).edit()
        editor.putBoolean("lucesState", switchLuces.isChecked)
        editor.apply()
    }
}
