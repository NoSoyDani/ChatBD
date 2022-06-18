package com.us.chatcbd.act

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import com.us.chatcbd.R
import com.us.chatcbd.RetrofitInstance
import com.us.chatcbd.adp.ChatAdp
import com.us.chatcbd.modelo.Chat
import com.us.chatcbd.modelo.Notificacion
import com.us.chatcbd.modelo.NotificacionPush
import com.us.chatcbd.modelo.Usuario
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatActividad : AppCompatActivity() {
    private lateinit var usuarioFireBase: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    var listaChat = ArrayList<Chat>()
    var topico = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_actividad)


        val vistaChat: RecyclerView = findViewById(R.id.chatRecyclerView)
        vistaChat.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false)

        val flecha: ImageView = findViewById(R.id.imagenFlechaVolver)
        flecha.setOnClickListener() {
            onBackPressed()
        }

        var idUsuario = getIntent().getStringExtra("IdUsuario")
        var nombreUsuario = getIntent().getStringExtra("NombreUsuario")

        usuarioFireBase = FirebaseAuth.getInstance().currentUser!!
        databaseReference =
            FirebaseDatabase.getInstance().getReference("Usuarios").child(idUsuario!!)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(Usuario::class.java)
                val txtNombreUsuario: TextView = findViewById(R.id.nombreUsuarioDest)
                val imagenUsuario: CircleImageView = findViewById(R.id.imagenUsuarioPerfilChat)
                txtNombreUsuario.text = usuario!!.nombreUsuario
                if (usuario.imagenPerfil != "") {
                    Glide.with(this@ChatActividad).load(usuario.imagenPerfil).into(imagenUsuario)
                } else {
                    imagenUsuario.setImageResource(R.drawable.icono)
                }


            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

        })

        val botonEnviarMensaje: ImageButton =findViewById(R.id.botonEnviarMensaje)
        botonEnviarMensaje.setOnClickListener{
            val campoTexto: EditText =findViewById(R.id.campoTexto)
            var mensaje:String = campoTexto.text.toString()
            if (mensaje.isEmpty()){
                Toast.makeText(applicationContext, "Mensaje vacío",Toast.LENGTH_SHORT).show()
                campoTexto.setText("")

            }else{
                enviarMensaje(usuarioFireBase!!.uid,idUsuario,mensaje)
                campoTexto.setText("")
                topico="/topics/$idUsuario"
                NotificacionPush(Notificacion(nombreUsuario!!,mensaje),topico).also { enviarNot(it)}
            }
        }
        getListaMensajesChatFireBase(usuarioFireBase!!.uid,idUsuario)
    }

    private fun enviarMensaje(emisorId: String, receptorId: String, mensaje: String){
        var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        var dicc:HashMap<String,String> = HashMap()
        dicc.put("emisorId",emisorId)
        dicc.put("receptorId",receptorId)
        dicc.put("mensaje",mensaje)
        databaseReference!!.child("Chat").push().setValue(dicc)

    }
    fun getListaMensajesChatFireBase(emisorId: String,receptorId: String){

        val databaseReference = FirebaseDatabase.getInstance().getReference("Chat")
        val vistaChat: RecyclerView = findViewById(R.id.chatRecyclerView)
        vistaChat.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false)

        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listaChat.clear()
                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val chat=dataSnapShot.getValue(Chat::class.java)
                    if(chat!!.emisorId.equals(emisorId) && chat!!.receptorId.equals(receptorId) || chat!!.emisorId.equals(receptorId) && chat!!.receptorId.equals(emisorId)){
                        listaChat.add(chat)
                    }
                }
                val chatAdp = ChatAdp(this@ChatActividad,listaChat)
                vistaChat.adapter = chatAdp
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private  fun enviarNot(notificacion:NotificacionPush) = CoroutineScope(Dispatchers.IO).launch {
        try{
            val respuesta = RetrofitInstance.api.postNotificacion((notificacion))
            if(respuesta.isSuccessful){
                Toast.makeText(this@ChatActividad, "Response ${Gson().toJson(respuesta)}",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@ChatActividad, respuesta.errorBody().toString(),Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            Toast.makeText(this@ChatActividad, e.message,Toast.LENGTH_SHORT).show()

        }




    }
}

