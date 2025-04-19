package com.example.gasolineraapplication.negocio

import android.content.Context
import com.example.gasolineraapplication.datos.SucursalDato

class SucursalNegocio(context: Context) {
    private val sucursalDato = SucursalDato(context)

    fun listarSucursalesPorTipo(nombreCombustible: String): List<Pair<Int, String>> {
        return sucursalDato.obtenerSucursalesPorTipo(nombreCombustible)
    }
    fun obtenerIdSucursalCombustible(idSucursal: Int, nombreCombustible: String): Int {
        return sucursalDato.obtenerIdSucursalCombustible(idSucursal, nombreCombustible)
    }


}
