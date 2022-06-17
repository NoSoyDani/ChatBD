package com.us.chatcbd.act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.us.chatcbd.R
import com.us.chatcbd.adp.UsuarioAdp
import com.us.chatcbd.modelo.Usuario
import de.hdodenhof.circleimageview.CircleImageView

class ChatUsuarios : AppCompatActivity() {

    var listaUsuarios= ArrayList<Usuario>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listado_usuarios)

        val vistaUsuario: RecyclerView = findViewById(R.id.usuarioRecyclerView)
        vistaUsuario.layoutManager= LinearLayoutManager(this,RecyclerView.VERTICAL,false)

        val flecha:ImageView = findViewById(R.id.imagenFlechaVolver)
        flecha.setOnClickListener(){
            onBackPressed()
        }

        val perfil:ImageView = findViewById(R.id.imagenUsuarioPerfilListado)
        perfil.setOnClickListener(){
            startActivity(Intent(this@ChatUsuarios, PerfilUsuario::class.java))
        }
        getListaUsuariosFireBase(vistaUsuario)
    }

    fun getListaUsuariosFireBase(vistaUsuario: RecyclerView){

        val base: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios")

        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listaUsuarios.clear()
                val usuarioLogeado=snapshot.getValue(Usuario::class.java)
                val imagenUsuario: CircleImageView =findViewById(R.id.imagenUsuarioPerfilListado)
                if (usuarioLogeado!!.imagenPerfil!=""){
                    Glide.with(this@ChatUsuarios).load(usuarioLogeado.imagenPerfil).into(imagenUsuario)
                }else{
                    imagenUsuario.setImageResource(R.drawable.icono)
                }

                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val usuario=dataSnapShot.getValue(Usuario::class.java)
                    if(!usuario!!.idUsuario.equals(base.uid)){
                        listaUsuarios.add(usuario)
                    }
                }
                val usuarioAdp = UsuarioAdp(this@ChatUsuarios,listaUsuarios)
                vistaUsuario.adapter = usuarioAdp
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }


        })
    }
}