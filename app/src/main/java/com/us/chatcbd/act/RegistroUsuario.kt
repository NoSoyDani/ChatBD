package com.us.chatcbd.act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.us.chatcbd.R

class RegistroUsuario : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_usuarios)
        auth = FirebaseAuth.getInstance()
        val botonRegistroUsuario: Button =findViewById(R.id.btnRegistarUsuario)
        botonRegistroUsuario.setOnClickListener {
            val nombreUsuario: EditText = findViewById(R.id.camNombre)
            val email: EditText = findViewById(R.id.camEmail)
            val password: EditText = findViewById(R.id.camPassword)
            val confirmPassword: EditText = findViewById(R.id.camConPassword)
            var correcto=true

            if (nombreUsuario.text.toString().isNullOrEmpty()){
                Toast.makeText(applicationContext,"El campo nombre de usuario es obligatorio",
                    Toast.LENGTH_SHORT).show()
                correcto=false
            }
            if (email.text.toString().isNullOrEmpty() && correcto){
                Toast.makeText(applicationContext,"El campo email es obligatorio", Toast.LENGTH_SHORT).show()
                correcto=false
            }
            if (password.text.toString().isNullOrEmpty() && correcto){
                Toast.makeText(applicationContext,"El campo contraseña es obligatorio", Toast.LENGTH_SHORT).show()
                correcto=false
            }
            if (confirmPassword.text.toString().isNullOrEmpty() && correcto){
                Toast.makeText(applicationContext,"El campo confirmar contraseña es obligatorio",
                    Toast.LENGTH_SHORT).show()
                correcto=false
            }
            if (!password.text.toString().equals(confirmPassword.text.toString()) && correcto){
                Toast.makeText(applicationContext,"Los campos contraseña y confirmar contraseña no coinciden",
                    Toast.LENGTH_SHORT).show()
                correcto=false
            }
            if (password.text.toString().length<6 && correcto){
                Toast.makeText(applicationContext,"El campo contraseña debe tener al menos una longitud de 6 caracteres",
                    Toast.LENGTH_SHORT).show()
                correcto=false
            }
            if (confirmPassword.text.toString().length<6 && correcto){
                Toast.makeText(applicationContext,"El campo confirmar contraseña debe tener al menos una longitud de 6 caracteres",
                    Toast.LENGTH_SHORT).show()
                correcto=false
            }
            if(correcto){
            registroUsuario(nombreUsuario.text.toString().trim(),email.text.toString().trim(),password.text.toString().trim())
            }
        }

        val botonLoginUsuario: Button =findViewById(R.id.btnLogIn)
        botonLoginUsuario.setOnClickListener {
            startActivity(Intent(this@RegistroUsuario, LoginUsuario::class.java))
            finish()
        }

    }

    private fun registroUsuario(nombreUsuario:String, email:String, password:String){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
            if(it.isSuccessful){
                val idUsuario:String = auth.currentUser!!.uid
                databaseReference= FirebaseDatabase.getInstance().getReference("Usuarios").child(idUsuario)

                val diccionario:HashMap<String,String> = HashMap()
                diccionario.put("idUsuario", idUsuario)
                diccionario.put("nombreUsuario", nombreUsuario)
                diccionario.put("imagenPerfil","")
                databaseReference.setValue(diccionario).addOnCompleteListener(this){
                    if(it.isSuccessful){
                        startActivity(Intent(this@RegistroUsuario, ChatUsuarios::class.java))
                        finish()
                    }else{
                        Toast.makeText(applicationContext,"Lo sentimos el usuario no se ha podido añadir a la base de datos",
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
}