package com.us.chatcbd

import com.us.chatcbd.Constantes.Constantes
import com.us.chatcbd.interfaz.NotifiacionesAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder().baseUrl(Constantes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }


        val api by lazy {
            retrofit.create(NotifiacionesAPI::class.java)
        }
    }
}