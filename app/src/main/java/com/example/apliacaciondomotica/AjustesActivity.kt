package com.example.apliacaciondomotica

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class AjustesActivity : AppCompatActivity() {

    private lateinit var idiomaSpinner: Spinner
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings) // Asegúrate de tener el layout correcto

        idiomaSpinner = findViewById(R.id.spinner_dispositivos) // Asegúrate de que el ID sea correcto
        btnGuardar = findViewById(R.id.btn_guardar_cambios)

        btnGuardar.setOnClickListener {
            val selectedLanguage = idiomaSpinner.selectedItem.toString()
            cambiarIdioma(selectedLanguage)
            volverAMainActivity()
        }
    }

    private fun cambiarIdioma(idioma: String) {
        val locale = when (idioma) {
            "Español" -> Locale("es")
            "Inglés" -> Locale("GB")
            "Francés" -> Locale("fr")
            else -> Locale.getDefault()
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)

        // Opcional: Guarda el idioma en SharedPreferences si deseas que persista
        val sharedPref = getSharedPreferences("app_preferences", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("selected_language", idioma)
            apply()
        }
        Toast.makeText(this, "Idioma cambiado a $idioma", Toast.LENGTH_SHORT).show()
    }

    private fun volverAMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Cierra AjustesActivity para que no vuelva al presionar "atrás"
    }
}
