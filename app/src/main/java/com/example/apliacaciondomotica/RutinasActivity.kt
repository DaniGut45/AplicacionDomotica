package com.example.apliacaciondomotica

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
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

        // Listener para ir a CrearRutinaActivity
        btnCrearRutina.setOnClickListener {
            val intent = Intent(this, CrearRutinaActivity::class.java)
            startActivity(intent)
        }

        // Recuperar datos del Intent si los hay
        val dispositivo = intent.getStringExtra("dispositivo")
        val hora = intent.getStringExtra("hora")
        val estadoSwitch = intent.getBooleanExtra("estado", false)
        val valorSeekBar = intent.getIntExtra("valor", -1)

        // Si hay datos de rutina, añadirla al layout
        if (dispositivo != null && hora != null) {
            agregarRutina(dispositivo, hora, estadoSwitch, valorSeekBar)
        }
    }


    private fun agregarRutina(dispositivo: String, hora: String, estado: Boolean, valor: Int) {
        // Inflate el layout de la rutina desde un archivo XML si lo prefieres
        val inflater = LayoutInflater.from(this)
        val nuevaRutinaView = inflater.inflate(R.layout.rutina_item, rutinaLayout, false)

        // Buscar los elementos dentro del layout inflado
        val txtRutina = nuevaRutinaView.findViewById<TextView>(R.id.txt_rutina_info)
        val btnModificar = nuevaRutinaView.findViewById<Button>(R.id.btn_modificar)
        val btnBorrar = nuevaRutinaView.findViewById<Button>(R.id.btn_borrar)

        // Formatear los datos de la rutina para mostrar
        val estadoTexto = if (estado) "Encendido" else "Apagado"
        val valorTexto = when (dispositivo) {
            "TV" -> "Volumen: $valor"
            "Luces" -> estadoTexto
            "Persianas" -> "Altura: ${valor * 10}%"
            "Termostato" -> "Temperatura: $valor°"
            else -> ""
        }
        txtRutina.text = "Dispositivo: $dispositivo\nHora: $hora\n$valorTexto"

        // Configurar los botones
        btnModificar.setOnClickListener {
            // Lógica para modificar (redirigir al CrearRutinaActivity)
            // Pasa los datos actuales para editar
            val intent = Intent(this, CrearRutinaActivity::class.java)
            intent.putExtra("dispositivo", dispositivo)
            intent.putExtra("hora", hora)
            intent.putExtra("estado", estado)
            intent.putExtra("valor", valor)
            startActivity(intent)
        }

        btnBorrar.setOnClickListener {
            // Eliminar esta rutina de la vista
            rutinaLayout.removeView(nuevaRutinaView)
        }

        // Añadir la nueva rutina al layout principal
        rutinaLayout.addView(nuevaRutinaView)
    }
}
