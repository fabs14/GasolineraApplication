package com.example.gasolineraapplication.presentacion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gasolineraapplication.R
import com.example.gasolineraapplication.negocio.ResultadoNegocio

class ResultadoPresentacion : BaseActivity() { // 👈 CAMBIADO: heredamos de BaseActivity

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
            editMetros.setText(metros.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado) // 👈 Esto asegura que cargue la vista dentro del drawer

        // Vincular vistas
        editLitros = findViewById(R.id.editLitros)
        editMetros = findViewById(R.id.editMetros)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnVerResultado = findViewById(R.id.btnVerResultado)
        btnAbrirMapa = findViewById(R.id.btnAbrirMapa)
        txtResultado = findViewById(R.id.txtResultado)

        resultadoNegocio = ResultadoNegocio(this)

        // Obtener IDs del intent
        idSucursal = intent.getIntExtra("idSucursal", -1)
        idSucursalCombustible = intent.getIntExtra("idSucursalCombustible", -1)

        // Abrir mapa para dibujar fila
        btnAbrirMapa.setOnClickListener {
            val intent = Intent(this, MapaDibujoActivity::class.java)
            mapaLauncher.launch(intent)
        }

        // Calcular y guardar resultado
        btnCalcular.setOnClickListener {
            val litros = editLitros.text.toString().toDoubleOrNull()
            val metros = editMetros.text.toString().toDoubleOrNull()

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
                Toast.makeText(this, "Error al guardar resultado", Toast.LENGTH_LONG).show()
            }
        }

        // Ver último resultado
        btnVerResultado.setOnClickListener {
            val resultado = resultadoNegocio.obtenerUltimoResultadoCalculado()

            if (resultado != null) {
                val tiempo = resultado.first
                val litrosRestantes = resultado.second

                txtResultado.text = "⏱ Tiempo estimado: $tiempo min\n" +
                        "\uD83D\uDEE3️ Litros restantes: $litrosRestantes L"
            } else {
                Toast.makeText(this, "No hay resultados registrados aún", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
