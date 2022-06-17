package com.us.chatcbd.act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.us.chatcbd.R

class LoginUsuario : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var usuarioFireBase: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_usuario)
        auth = FirebaseAuth.getInstance()

        val botonLoginUsuario: Button =findViewById(R.id.btnLogIn_Log)
        botonLoginUsuario.setOnClickListener{
            val email: EditText = findViewById(R.id.camEmail_login)
            val password: EditText = findViewById(R.id.camPassword_login)
            if (email.text.toString().isNullOrEmpty()){
                Toast.makeText(applicationContext,"El campo email no puede estar vacio", Toast.LENGTH_SHORT).show()
            }
            else if (password.text.toString().isNullOrEmpty()){
                Toast.makeText(applicationContext,"El campo contraseña no puede estar vacio", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(email.text.toString().trim(),password.text.toString().trim())
                    .addOnCompleteListener(this){
                        if (it.isSuccessful){
                            startActivity(Intent(this@LoginUsuario, ChatUsuarios::class.java))
                            finish()
                        }else{
                            Toast.makeText(applicationContext,"El correo o/y la contraseña no son validos",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        val botonRegistroUsuario: Button =findViewById(R.id.btnRegistarUsuario_Log)
        botonRegistroUsuario.setOnClickListener {
            startActivity(Intent(this@LoginUsuario, RegistroUsuario::class.java))
            finish()
        }


    }
}