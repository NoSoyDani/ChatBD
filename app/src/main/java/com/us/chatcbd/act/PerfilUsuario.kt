package com.us.chatcbd.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.us.chatcbd.R
import com.us.chatcbd.modelo.Usuario
import de.hdodenhof.circleimageview.CircleImageView

class PerfilUsuario : AppCompatActivity() {

    private lateinit var usuarioFireBase: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil_usuario)

        usuarioFireBase=FirebaseAuth.getInstance().currentUser!!
        databaseReference=FirebaseDatabase.getInstance().getReference("Usuarios").child(usuarioFireBase.uid)
        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario= snapshot.getValue(Usuario::class.java)
                val txtNombreUsuario: TextView = findViewById(R.id.nombreUsuario)
                val imagenUsuario:CircleImageView=findViewById(R.id.imagenUsuarioPerfil)
                txtNombreUsuario.text=usuario!!.nombreUsuario
                if (usuario.imagenPerfil!=""){
                    Glide.with(this@PerfilUsuario).load(usuario.imagenPerfil).into(imagenUsuario)
                }else{
                    imagenUsuario.setImageResource(R.drawable.icono)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

        })
        val flecha: ImageView = findViewById(R.id.imagenFlechaVolverPerfil)
        flecha.setOnClickListener(){
            onBackPressed()
        }
    }
}