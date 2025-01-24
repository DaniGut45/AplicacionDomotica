package com.example.apliacaciondomotica

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class RutinaReceiver : BroadcastReceiver() {

    private val ID_CANAL = "canal_rutinas_01"

    override fun onReceive(context: Context?, intent: Intent?) {
        val dispositivo = intent?.getStringExtra("dispositivo") ?: "Desconocido"
        val estado = intent?.getBooleanExtra("estado", false) ?: false
        val valor = intent?.getIntExtra("valor", 0) ?: 0

        val mensaje = when (dispositivo) {
            "TV" -> "Estado: ${if (estado) "Encendido" else "Apagado"}, Volumen: $valor"
            "Luces" -> "Estado: ${if (estado) "Encendido" else "Apagado"}"
            "Persianas" -> "Porcentaje abierto: $valor%"
            "Termostato" -> "Temperatura: $valor°"
            else -> "Configuración desconocida"
        }

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificacion = NotificationCompat.Builder(context, ID_CANAL)
            .setContentTitle("Rutina Activada: $dispositivo")
            .setContentText(mensaje)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(101, notificacion)
    }
}
