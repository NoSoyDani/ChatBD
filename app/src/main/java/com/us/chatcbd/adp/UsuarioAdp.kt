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
import com.us.chatcbd.R
import com.us.chatcbd.act.ChatActividad
import com.us.chatcbd.modelo.Usuario
import de.hdodenhof.circleimageview.CircleImageView

class UsuarioAdp(private val context: Context,
                 private val listaUsuario: ArrayList<Usuario>) : RecyclerView.Adapter<UsuarioAdp.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioAdp.ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.usuario, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount(): Int {
        return listaUsuario.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario = listaUsuario[position]
        holder.txtNombreUsuario.text = usuario.nombreUsuario
        Glide.with(context).load(usuario.imagenPerfil).placeholder(R.drawable.icono)
            .into(holder.imagenUsuario)
        holder.chatUsuario.setOnClickListener {
        val intent = Intent(context, ChatActividad::class.java)
            intent.putExtra("IdUsuario", usuario.idUsuario)
            context.startActivity(intent)
        }
    }

    class  ViewHolder(vista:View):RecyclerView.ViewHolder(vista){
        val txtNombreUsuario:TextView = vista.findViewById(R.id.nombreUsuario)
        val imagenUsuario:CircleImageView=vista.findViewById(R.id.imagenUsuario)
        val chatUsuario: LinearLayout = vista.findViewById(R.id.chatUsuario)
    }
}