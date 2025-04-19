package com.example.gasolineraapplication.presentacion

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.example.gasolineraapplication.R
import com.example.gasolineraapplication.negocio.HistorialNegocio

class HistorialPresentacion : BaseActivity() {

    private lateinit var layoutHistorial: LinearLayout
    private lateinit var historialNegocio: HistorialNegocio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        layoutHistorial = findViewById(R.id.layoutHistorial)
        historialNegocio = HistorialNegocio(this)

        mostrarHistorial()
    }

    private fun mostrarHistorial() {
        val lista = historialNegocio.obtenerHistorialFormateado()

        for (registro in lista) {
            val textView = TextView(this).apply {
                text = registro
                textSize = 16f
                setPadding(16, 16, 16, 16)
            }
            layoutHistorial.addView(textView)
        }
    }
}
