package com.example.apliacaciondomotica

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class LucesSettings : AppCompatActivity() {
    private lateinit var switchLuces: Switch
    private lateinit var buttonBorrar: Button
    private lateinit var buttonActualizar: Button
    private lateinit var progressBarActualizar: ProgressBar


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_luces)

        switchLuces = findViewById(R.id.switch_luces)
        buttonBorrar = findViewById(R.id.buttonBorrar)
        buttonActualizar = findViewById(R.id.buttonActualizar)
        progressBarActualizar = findViewById(R.id.progressBarActualizar)

        val buttonBorrar: Button
        buttonBorrar = findViewById(R.id.buttonBorrar)
        buttonBorrar.setOnClickListener{
            Toast.makeText(this, getString(R.string.dispositivo_borrado), Toast.LENGTH_SHORT).show()
        }

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

        // Configurar el botón de "Actualizar Firmware"
        buttonActualizar.setOnClickListener {
            // Mostrar el Toast y el ProgressBar
            Toast.makeText(this,
                getString(R.string.actualizando_firmware_de_las_luces), Toast.LENGTH_SHORT).show()
            progressBarActualizar.visibility = ProgressBar.VISIBLE

            // Generar un retardo aleatorio entre 2 y 5 segundos (2000 ms y 5000 ms)
            val delayMillis = Random.nextInt(1000, 15001)

            // Ocultar el ProgressBar después del tiempo aleatorio
            Handler(Looper.getMainLooper()).postDelayed({
                progressBarActualizar.visibility = ProgressBar.INVISIBLE
                Toast.makeText(this, "Firmware actualizado correctamente", Toast.LENGTH_SHORT).show()
            }, delayMillis.toLong())
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
