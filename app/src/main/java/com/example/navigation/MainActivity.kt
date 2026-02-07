package com.example.navigation

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput : TextView
    private lateinit var passInput : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        emailInput = findViewById<TextView>(R.id.email)
        passInput = findViewById<TextView>(R.id.password)
    }

    //Cuentas validas:
    //jaime@gmail.com, patito123
    //alvaro@gmail.com, alvaro
    fun login(view: View){

        val email = emailInput.text.toString()
        val pass = passInput.text.toString()

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Home::class.java).putExtra("email", email))
            }
            else Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
         }
    }

    override fun onStart(){
        super.onStart()

        val usuarioActual = Firebase.auth.currentUser

        if(usuarioActual != null){
            Toast.makeText(this, "Usuario anteriormente autenticado", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Home::class.java).putExtra("email", usuarioActual.email))
        }
        else{
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}