package com.example.gasolineraapplication.presentacion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gasolineraapplication.R
import com.example.gasolineraapplication.negocio.ResultadoNegocio

class ResultadoPresentacion : BaseActivity() {

    private lateinit var editLitros: EditText
    private lateinit var editMetros: EditText
    private lateinit var btnCalcular: Button
    private lateinit var btnVerResultado: Button
    private lateinit var btnAbrirMapa: Button
    private lateinit var txtResultado: TextView
    private lateinit var resultadoNegocio: ResultadoNegocio

    private var idSucursal: Int = -1
    private var idSucursalCombustible: Int = -1

    // Recibir distancia desde el mapa
    private val mapaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val metros = result.data?.getDoubleExtra("metrosFila", 0.0) ?: 0.0
            editMetros.setText(metros.toInt().toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        // Vincular vistas
        editLitros = findViewById(R.id.editLitros)
        editMetros = findViewById(R.id.editMetros)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnVerResultado = findViewById(R.id.btnVerResultado)
        btnAbrirMapa = findViewById(R.id.btnAbrirMapa)
        txtResultado = findViewById(R.id.txtResultado)

        // Ocultar resultado al inicio
        txtResultado.visibility = View.GONE

        resultadoNegocio = ResultadoNegocio(this)

        idSucursal = intent.getIntExtra("idSucursal", -1)
        idSucursalCombustible = intent.getIntExtra("idSucursalCombustible", -1)

        // Inhabilitar edición manual del campo metros
        editMetros.isFocusable = false
        editMetros.isClickable = false

        btnAbrirMapa.setOnClickListener {
            val ubicacion = resultadoNegocio.obtenerUbicacionSucursal(idSucursal)
            val intent = Intent(this, MapaDibujoActivity::class.java).apply {
                putExtra("latitud", ubicacion?.first ?: -1.0)
                putExtra("longitud", ubicacion?.second ?: -1.0)
            }
            mapaLauncher.launch(intent)
        }

        btnCalcular.setOnClickListener {
            val litros = editLitros.text.toString().toDoubleOrNull()
            val metros = editMetros.text.toString().toIntOrNull()?.toDouble()

            if (litros == null || metros == null || idSucursal == -1 || idSucursalCombustible == -1) {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exito = resultadoNegocio.calcularYGuardarResultado(
                idSucursal,
                idSucursalCombustible,
                litros,
                metros
            )

            if (exito) {
                Toast.makeText(this, "Resultado guardado correctamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Ya existe un cálculo reciente con los mismos datos", Toast.LENGTH_LONG).show()
            }
        }

        btnVerResultado.setOnClickListener {
            val resultado = resultadoNegocio.obtenerUltimoResultadoCalculado(idSucursalCombustible)

            if (resultado != null) {
                val (tiempo, litrosRestantes, alcanza) = resultado

                val texto = "⏱ Tiempo estimado: $tiempo min\n" +
                        "🛢️ Litros restantes: $litrosRestantes L\n" +
                        "✅ ¿Alcanza el combustible?: ${if (alcanza) "Sí" else "No"}"

                txtResultado.text = texto
                txtResultado.setBackgroundResource(R.drawable.card_historial_background)
                txtResultado.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "No hay resultados registrados aún", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
