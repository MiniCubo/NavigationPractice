package com.example.navigation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import android.util.Log
import android.content.ContentValues.TAG
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.TooManyListenersException

class Home : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val database = Firebase.database
    val myRef = database.getReference("peliculas")

    lateinit var peliculas: ArrayList<Peliculas>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        auth = Firebase.auth
        val extras = intent.extras

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val agregaPelicula = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        agregaPelicula.setOnClickListener {
            val peli = Peli("nombre", "lanzaminento", "genero")
            myRef.push().setValue(peli).addOnCompleteListener {
                task ->
                Toast.makeText(this, "Pelicula agregada", Toast.LENGTH_SHORT).show()
            }
        }

        val listView = findViewById<ListView>(R.id.lista)
        listView.setOnItemClickListener {
            parent, view, position, id ->
                Toast.makeText(this, peliculas[position].nombre.toString(), Toast.LENGTH_SHORT).show()
        }
        // Read from the database
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                peliculas = ArrayList<Peliculas>()
                val value = snapshot.value

                Log.d("real-time-database", "Value is: " + value)
                snapshot.children.forEach { entry ->
                    var item = Peliculas(entry.child("nombre").value.toString(),
                            entry.child("lanzamiento").value.toString(),
                        entry.child("gen").value.toString(),
                        entry.key.toString())
                    peliculas.add(item)
0            }

                llenarListView()


            }

            public fun llenarListView(){
                val lista = findViewById<ListView>(R.id.lista)
                val adaptador = PeliAdapter(this@Home, peliculas)
                lista.adapter = adaptador
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("real-time-database", "Failed to read value.", error.toException())
            }

        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}