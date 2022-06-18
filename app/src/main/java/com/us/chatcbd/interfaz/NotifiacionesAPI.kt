package com.us.chatcbd.interfaz

import com.us.chatcbd.Constantes.Constantes.Companion.CONTENT_TYPE
import com.us.chatcbd.Constantes.Constantes.Companion.SERVER_KEY
import com.us.chatcbd.modelo.NotificacionPush
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body

import retrofit2.http.Headers
import retrofit2.http.POST

interface NotifiacionesAPI {
    @Headers("Athorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotificacion(
        @Body notificacion: NotificacionPush
    ): Response<ResponseBody>
}