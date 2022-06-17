package com.us.chatcbd.adp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.us.chatcbd.R
import com.us.chatcbd.act.ChatActividad
import com.us.chatcbd.modelo.Chat
import com.us.chatcbd.modelo.Usuario
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdp(private val context: Context,
              private val listaChat: ArrayList<Chat>) : RecyclerView.Adapter<ChatAdp.ViewHolder>() {


    private val mensajeIzquierda = 0
    private val mensajeDerecha = 1
    private var usuarioFireBase: FirebaseUser? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdp.ViewHolder {

        if(viewType == mensajeDerecha){
            val vista = LayoutInflater.from(parent.context).inflate(R.layout.caja_derecha, parent, false)
            return ViewHolder(vista)
        }else{
            val vista = LayoutInflater.from(parent.context).inflate(R.layout.caja_izquierda, parent, false)
            return ViewHolder(vista)
        }
    }

    override fun getItemCount(): Int {
        return listaChat.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = listaChat[position]
        holder.mensajeChat.text = chat.mensaje

    }

    class  ViewHolder(vista:View):RecyclerView.ViewHolder(vista){
        val mensajeChat:TextView = vista.findViewById(R.id.mensajeChat)
        val imagenUsuario:CircleImageView=vista.findViewById(R.id.imagenUsuario)
    }

    override fun getItemViewType(position: Int): Int {
        usuarioFireBase= FirebaseAuth.getInstance().currentUser!!
        if(listaChat[position].emisorId == usuarioFireBase!!.uid) {
            return mensajeDerecha
        }else{
            return  mensajeIzquierda
        }
    }
}