package com.example.cursoandroidclase2.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cursoandroidclase2.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nuevo token: $token")
        // Aquí puedes enviar el token a tu backend
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM", "Mensaje recibido: ${remoteMessage.data}")

        // Si viene con notificación
        remoteMessage.notification?.let {
            Log.d("FCM", "Notificación: ${it.title} - ${it.body}")
            // Aquí podrías mostrar una notificación local
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun mostrarNotificacion(titulo: String, cuerpo: String) {
        val canalId = "canal_fcm"
        val nombreCanal = "Mensajes FCM"

        // Crear canal si es Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                canalId,
                nombreCanal,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para notificaciones de Firebase"

            }

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(canal)
        }

        val builder = NotificationCompat.Builder(this, canalId)
            //.setSmallIcon(R.drawable.ic_notification) // asegurate de tener este ícono
            .setContentTitle(titulo)
            .setContentText(cuerpo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        NotificationManagerCompat.from(this).notify(1, builder.build())

    }

}