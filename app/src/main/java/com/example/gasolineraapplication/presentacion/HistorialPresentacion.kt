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
            val card = TextView(this).apply {
                text = registro
                textSize = 16f
                setPadding(24, 16, 24, 16)
                setTextColor(getColor(android.R.color.black))
                setBackgroundResource(R.drawable.card_historial_background)
            }

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 24)
            layoutHistorial.addView(card, params)
        }
    }
}
