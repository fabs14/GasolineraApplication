package com.example.gasolineraapplication.negocio

import android.content.Context
import com.example.gasolineraapplication.datos.ResultadoDato

class ResultadoNegocio(context: Context) {

    private val resultadoDato = ResultadoDato(context)

    fun calcularYGuardarResultado(
        idSucursal: Int,
        idSucursalCombustible: Int,
        litrosIngresados: Double,
        metrosFila: Double
    ): Boolean {
        val cantidadBombas = resultadoDato.obtenerCantidadBombasSucursal(idSucursal)
        val tiempoCargaPorVehiculo = 2.0  // minutos
        val metrosPorVehiculo = 5.0
        val litrosPorVehiculo = 10.0

        val cantidadVehiculos = (metrosFila / metrosPorVehiculo).toInt()
        val tiempoTotalMin = (cantidadVehiculos * tiempoCargaPorVehiculo / cantidadBombas).toInt()
        val litrosRestantes = litrosIngresados - (cantidadVehiculos * litrosPorVehiculo)

        return resultadoDato.insertarResultado(
            idSucursalCombustible,
            litrosIngresados,
            metrosFila,
            tiempoTotalMin,
            litrosRestantes
        )
    }

    fun obtenerUltimoResultadoCalculado(): Pair<Int, Double>? {
        val cursor = resultadoDato.obtenerUltimoResultado()
        if (cursor.moveToFirst()) {
            val tiempo = cursor.getInt(0)
            val litros = cursor.getDouble(1)
            cursor.close()
            return Pair(tiempo, litros)
        }
        cursor.close()
        return null
    }
}
