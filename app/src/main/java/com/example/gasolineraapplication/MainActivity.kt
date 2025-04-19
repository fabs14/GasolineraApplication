package com.example.gasolineraapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gasolineraapplication.presentacion.TipoCombustiblePresentacion

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Redirigir directamente a la pantalla principal del proyecto
        val intent = Intent(this, TipoCombustiblePresentacion::class.java)
        startActivity(intent)
        finish() // opcional: para cerrar esta pantalla y no volver con "Atrás"
    }
}
