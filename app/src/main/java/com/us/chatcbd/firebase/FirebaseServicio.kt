package com.us.chatcbd.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.us.chatcbd.R
import com.us.chatcbd.act.ChatUsuarios
import kotlin.random.Random

class FirebaseServicio : FirebaseMessagingService() {


    val CANAL = "-"
    companion object{
        var preferencias:SharedPreferences?=null
        var token: String?
        get(){
            return preferencias?.getString("token","")
        }
        set(valor){
            preferencias?.edit()?.putString("token",valor)?.apply()
        }
    }

    override fun onNewToken (p0: String){
        super.onNewToken(p0)
        token = p0
    }

    override fun onMessageReceived(p0: RemoteMessage){
        super.onMessageReceived(p0)
        val intent = Intent(this,ChatUsuarios::class.java)
        val gestor = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificacionId = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            canalNotificacion(gestor)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val intento = PendingIntent.getActivity(this,0,intent,FLAG_ONE_SHOT)
        val notificacion = NotificationCompat.Builder(this, CANAL).setContentTitle(p0.data["titulo"]).setContentText(p0.data["mensaje"]).setSmallIcon(
            R.drawable.noti).setAutoCancel(true).setContentIntent(intento).build()

        gestor.notify(notificacionId, notificacion)


    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun canalNotificacion(gestor: NotificationManager) {
        val canalFirebase = "ChannelFirebaseChat"
        val canal = NotificationChannel(CANAL,canalFirebase,IMPORTANCE_HIGH).apply {
            description = "-"
            enableLights(true)
            lightColor = Color.WHITE
        }
        gestor.createNotificationChannel(canal)
    }

}