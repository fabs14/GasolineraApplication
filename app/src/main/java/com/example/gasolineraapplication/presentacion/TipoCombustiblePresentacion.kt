package com.example.gasolineraapplication.presentacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.gasolineraapplication.R
import com.example.gasolineraapplication.negocio.TipoCombustibleNegocio

class TipoCombustiblePresentacion : BaseActivity() { // ✅ Hereda de BaseActivity

    private lateinit var layoutTipos: LinearLayout
    private lateinit var tipoNegocio: TipoCombustibleNegocio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Cargamos el layout dentro del contenedor del DrawerLayout de BaseActivity
        setContentView(R.layout.activity_tipo_combustible)

        layoutTipos = findViewById(R.id.layoutTipos)
        tipoNegocio = TipoCombustibleNegocio(this)

        mostrarTipos()
    }

    private fun mostrarTipos() {
        val tipos = tipoNegocio.listarTiposCombustible()

        for (tipo in tipos) {
            val boton = Button(this).apply {
                text = tipo
                setBackgroundResource(R.drawable.rounded_button)
                setTextColor(ContextCompat.getColor(this@TipoCombustiblePresentacion, android.R.color.white))
                textSize = 16f
                setPadding(24, 16, 24, 16)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(0, 0, 0, 24) // margen entre botones
                this.layoutParams = layoutParams

                setOnClickListener {
                    Toast.makeText(this@TipoCombustiblePresentacion, "Seleccionaste: $tipo", Toast.LENGTH_SHORT).show()

                    // Redirigir a la pantalla de sucursales
                    val intent = Intent(this@TipoCombustiblePresentacion, SucursalPresentacion::class.java)
                    intent.putExtra("tipo_combustible", tipo)
                    startActivity(intent)
                }
            }
            layoutTipos.addView(boton)
        }
    }
}
