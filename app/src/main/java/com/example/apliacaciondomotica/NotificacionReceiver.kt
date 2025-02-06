package com.example.apliacaciondomotica

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificacionReceiver : BroadcastReceiver() {

    private val ID_CANAL = "canal_notificaciones_01"
    private val idNotificacion = 101

    override fun onReceive(context: Context, intent: Intent) {
        val dispositivo = intent.getStringExtra("dispositivo") ?: "Dispositivo"
        val estado = intent.getStringExtra("estado") ?: "Estado desconocido"

        // Intent para reiniciar la aplicación cuando se haga clic en la notificación
        val intentReinicio = Intent(context, MainActivity::class.java)
        val pendingIntentReinicio = PendingIntent.getActivity(context, 0, intentReinicio, PendingIntent.FLAG_IMMUTABLE)

        // Crear notificación
        val administradorNotificaciones = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificacion = NotificationCompat.Builder(context, ID_CANAL)
            .setContentTitle("Rutina Ejecutándose")
            .setContentText("$dispositivo está $estado")
            .setSmallIcon(R.drawable.logo) // Asegúrate de tener un icono para la notificación
            .setContentIntent(pendingIntentReinicio)  // Se añade el PendingIntent a la notificación
            .setAutoCancel(true)  // La notificación se cierra automáticamente al hacer clic en ella
            .build()

        // Muestra la notificación
        administradorNotificaciones.notify(idNotificacion, notificacion)
    }
}
