package com.example.gasolineraapplication.negocio

import android.content.Context
import com.example.gasolineraapplication.datos.HistorialDato

class HistorialNegocio(context: Context) {

    private val historialDato = HistorialDato(context)

    fun obtenerHistorialFormateado(): List<String> {
        val lista = mutableListOf<String>()
        val cursor = historialDato.obtenerHistorial()

        if (cursor.moveToFirst()) {
            do {
                val fecha = cursor.getString(0)
                val tiempo = cursor.getInt(1)
                val litros = cursor.getDouble(2)
                val tipo = cursor.getString(3)
                val sucursal = cursor.getString(4)
                val alcanza = cursor.getInt(5) == 1

                val texto = """
                    🕒 Fecha: $fecha
                    ⛽ Tipo: $tipo
                    🏪 Sucursal: $sucursal
                    ⏱ Tiempo estimado: $tiempo min
                    🛢️ Litros restantes: $litros L
                    ✅ ¿Alcanza el combustible?: ${if (alcanza) "Sí" else "No"}
                """.trimIndent()

                lista.add(texto)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }
}
