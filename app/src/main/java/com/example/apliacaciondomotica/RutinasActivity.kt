package com.example.apliacaciondomotica

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

data class Rutina(val dispositivo: String, val hora: String, val estado: Boolean, val valor: Int)

class RutinasActivity : AppCompatActivity() {

    private lateinit var btnCrearRutina: Button
    private lateinit var rutinaLayout: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rutinas)

        btnCrearRutina = findViewById(R.id.btn_crear_rutina)
        rutinaLayout = findViewById(R.id.rutina_layout)
        sharedPreferences = getSharedPreferences("RutinasPref", MODE_PRIVATE)

        // Listener para crear nueva rutina
        btnCrearRutina.setOnClickListener {
            val intent = Intent(this, CrearRutinaActivity::class.java)
            startActivity(intent)
        }

        // Cargar rutinas guardadas al iniciar
        cargarRutinas()

        // Verificar si hay datos enviados desde CrearRutinaActivity
        val dispositivo = intent.getStringExtra("dispositivo")
        val hora = intent.getStringExtra("hora")
        val estado = intent.getBooleanExtra("estado", false)
        val valor = intent.getIntExtra("valor", -1)
        val position = intent.getIntExtra("position", -1) // Posición de la rutina (si se modifica)

        if (dispositivo != null && hora != null) {
            val rutina = Rutina(dispositivo, hora, estado, valor)
            if (position != -1) {
                // Actualizar rutina existente
                actualizarRutina(position, rutina)
            } else {
                // Agregar una nueva rutina
                agregarRutina(rutina)
            }
        }
    }

    private fun cargarRutinas() {
        val rutinasString = sharedPreferences.getString("rutinas", null)
        if (!rutinasString.isNullOrEmpty()) {
            // Separar las rutinas por ";"
            val rutinas = rutinasString.split(";").mapNotNull { convertirTextoARutina(it) }
            rutinas.forEach { agregarRutina(it, guardar = false) }
        }
    }

    private fun guardarRutinas() {
        val rutinas = obtenerRutinasActuales()
        val rutinasString = rutinas.joinToString(";") { convertirRutinaATexto(it) }
        sharedPreferences.edit().putString("rutinas", rutinasString).apply()
    }

    private fun obtenerRutinasActuales(): List<Rutina> {
        val rutinas = mutableListOf<Rutina>()
        for (i in 0 until rutinaLayout.childCount) {
            val rutinaView = rutinaLayout.getChildAt(i)
            val txtInfo = rutinaView.findViewById<TextView>(R.id.txt_rutina_info)
            val info = txtInfo.text.toString().split("\n")
            val dispositivo = info[0].removePrefix("Dispositivo: ").trim()
            val hora = info[1].removePrefix("Hora: ").trim()
            val valorTexto = info.getOrNull(2) ?: ""
            val estado = valorTexto.contains("Encendido")
            val valor = valorTexto.filter { it.isDigit() }.toIntOrNull() ?: 0
            rutinas.add(Rutina(dispositivo, hora, estado, valor))
        }
        return rutinas
    }

    private fun agregarRutina(rutina: Rutina, guardar: Boolean = true, position: Int? = null) {
        // Si se pasa una posición, actualizar la rutina existente
        if (position != null) {
            val rutinaView = rutinaLayout.getChildAt(position)
            val txtRutinaInfo = rutinaView.findViewById<TextView>(R.id.txt_rutina_info)
            val estadoTexto = if (rutina.estado) "Encendido" else "Apagado"
            val valorTexto = when (rutina.dispositivo) {
                "TV" -> "Volumen: ${rutina.valor}"
                "Luces" -> estadoTexto
                "Persianas" -> "Altura: ${rutina.valor}%"
                "Termostato" -> "Temperatura: ${rutina.valor}°"
                else -> ""
            }
            txtRutinaInfo.text = "Dispositivo: ${rutina.dispositivo}\nHora: ${rutina.hora}\n$valorTexto"
            if (guardar) guardarRutinas()
            return
        }

        // Si no hay posición, añadir una nueva rutina
        val inflater = LayoutInflater.from(this)
        val nuevaRutinaView = inflater.inflate(R.layout.rutina_item, rutinaLayout, false)

        val txtRutinaInfo = nuevaRutinaView.findViewById<TextView>(R.id.txt_rutina_info)
        val btnModificar = nuevaRutinaView.findViewById<Button>(R.id.btn_modificar)
        val btnBorrar = nuevaRutinaView.findViewById<Button>(R.id.btn_borrar)

        val estadoTexto = if (rutina.estado) "Encendido" else "Apagado"
        val valorTexto = when (rutina.dispositivo) {
            "TV" -> "Volumen: ${rutina.valor}"
            "Luces" -> estadoTexto
            "Persianas" -> "Altura: ${rutina.valor}%"
            "Termostato" -> "Temperatura: ${rutina.valor}°"
            else -> ""
        }
        txtRutinaInfo.text = "Dispositivo: ${rutina.dispositivo}\nHora: ${rutina.hora}\n$valorTexto"

        btnModificar.setOnClickListener {
            val intent = Intent(this, CrearRutinaActivity::class.java)
            intent.putExtra("dispositivo", rutina.dispositivo)
            intent.putExtra("hora", rutina.hora)
            intent.putExtra("estado", rutina.estado)
            intent.putExtra("valor", rutina.valor)
            intent.putExtra("position", rutinaLayout.indexOfChild(nuevaRutinaView)) // Pasar la posición
            startActivity(intent)
        }


        btnBorrar.setOnClickListener {
            rutinaLayout.removeView(nuevaRutinaView)
            guardarRutinas()
        }

        rutinaLayout.addView(nuevaRutinaView)
        if (guardar) guardarRutinas()
    }


    private fun convertirTextoARutina(texto: String): Rutina? {
        val partes = texto.split(",")
        return if (partes.size == 4) {
            val dispositivo = partes[0]
            val hora = partes[1]
            val estado = partes[2].toBoolean()
            val valor = partes[3].toIntOrNull() ?: 0
            Rutina(dispositivo, hora, estado, valor)
        } else {
            null
        }
    }

    private fun convertirRutinaATexto(rutina: Rutina): String {
        return "${rutina.dispositivo},${rutina.hora},${rutina.estado},${rutina.valor}"
    }

    private fun actualizarRutina(position: Int, rutina: Rutina) {
        val rutinaView = rutinaLayout.getChildAt(position)

        // Actualizar los datos del TextView en el rutina_item
        val txtRutinaInfo = rutinaView.findViewById<TextView>(R.id.txt_rutina_info)
        val estadoTexto = if (rutina.estado) "Encendido" else "Apagado"
        val valorTexto = when (rutina.dispositivo) {
            "TV" -> "Volumen: ${rutina.valor}"
            "Luces" -> estadoTexto
            "Persianas" -> "Altura: ${rutina.valor}%"
            "Termostato" -> "Temperatura: ${rutina.valor}°"
            else -> ""
        }
        txtRutinaInfo.text = "Dispositivo: ${rutina.dispositivo}\nHora: ${rutina.hora}\n$valorTexto"

        // Guardar los cambios
        guardarRutinas()
    }

}
