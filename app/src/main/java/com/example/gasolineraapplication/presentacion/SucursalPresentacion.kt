package com.example.gasolineraapplication.presentacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.gasolineraapplication.R
import com.example.gasolineraapplication.negocio.SucursalNegocio

class SucursalPresentacion : BaseActivity() {

    private lateinit var layoutSucursales: LinearLayout
    private lateinit var sucursalNegocio: SucursalNegocio
    private lateinit var tipoSeleccionado: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sucursal)

        layoutSucursales = findViewById(R.id.layoutSucursales)
        sucursalNegocio = SucursalNegocio(this)

        tipoSeleccionado = intent.getStringExtra("tipo_combustible") ?: ""

        mostrarSucursales(tipoSeleccionado)
    }

    private fun mostrarSucursales(tipo: String) {
        val sucursales = sucursalNegocio.listarSucursalesPorTipo(tipo)

        for ((idSucursal, nombreSucursal) in sucursales) {
            val btn = Button(this).apply {
                text = nombreSucursal
                setBackgroundResource(R.drawable.rounded_button)
                setTextColor(ContextCompat.getColor(this@SucursalPresentacion, android.R.color.white))
                textSize = 16f
                setPadding(24, 16, 24, 16)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(0, 0, 0, 24) // margen entre botones
                this.layoutParams = layoutParams

                setOnClickListener {
                    val idSucursalCombustible =
                        sucursalNegocio.obtenerIdSucursalCombustible(idSucursal, tipo)

                    if (idSucursalCombustible != -1) {
                        val intent = Intent(this@SucursalPresentacion, ResultadoPresentacion::class.java)
                        intent.putExtra("idSucursal", idSucursal)
                        intent.putExtra("idSucursalCombustible", idSucursalCombustible)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@SucursalPresentacion,
                            "Error al obtener relación sucursal-combustible",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            layoutSucursales.addView(btn)
        }
    }
}
