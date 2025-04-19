package com.example.gasolineraapplication.negocio

import android.content.Context
import android.util.Log
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

        // Constantes
        val tiempoCargaPorVehiculo = 2.0
        val metrosPorVehiculo = 5.0
        val litrosPorVehiculo = 10.0

        // Cálculos
        val cantidadVehiculos = (metrosFila / metrosPorVehiculo).toInt()
        val tiempoTotalMin = (cantidadVehiculos * tiempoCargaPorVehiculo / cantidadBombas).toInt()

        val litrosCalculados = litrosIngresados - (cantidadVehiculos * litrosPorVehiculo)
        val litrosRestantes = String.format("%.2f", if (litrosCalculados < 0) 0.0 else litrosCalculados).toDouble()

        val alcanza = litrosRestantes >= litrosPorVehiculo


        // Guardar
        return resultadoDato.insertarResultado(
            idSucursalCombustible,
            litrosIngresados,
            metrosFila,
            tiempoTotalMin,
            litrosRestantes,
            alcanza
        )
    }

    fun obtenerUltimoResultadoCalculado(idSucursalCombustible: Int): Triple<Int, Double, Boolean>? {
        val cursor = resultadoDato.obtenerUltimoResultado(idSucursalCombustible)
        if (cursor.moveToFirst()) {
            val tiempo = cursor.getInt(0)
            val litros = cursor.getDouble(1)
            val alcanza = cursor.getInt(2) == 1
            cursor.close()
            return Triple(tiempo, litros, alcanza)
        }
        cursor.close()
        return null
    }

    fun obtenerUbicacionSucursal(idSucursal: Int): Pair<Double, Double>? {
        return resultadoDato.obtenerUbicacionSucursal(idSucursal)
    }
}
