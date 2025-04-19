package com.example.gasolineraapplication.datos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.gasolinera.gasolineraapplication.datos.BaseDeDatosHelper

class ResultadoDato(context: Context) {

    private val helper = BaseDeDatosHelper(context)

    fun insertarResultado(
        idSucursalCombustible: Int,
        litrosIngresados: Double,
        metrosFila: Double,
        tiempoEstimadoMin: Int,
        litrosRestantes: Double
    ): Boolean {
        val db = helper.writableDatabase
        val valores = ContentValues().apply {
            put("id_sucursalcombustible", idSucursalCombustible)
            put("litros_ingresados", litrosIngresados)
            put("metros_fila", metrosFila)
            put("tiempo_estimado_min", tiempoEstimadoMin)
            put("litros_restantes", litrosRestantes)
        }
        val resultado = db.insert("Resultado", null, valores)
        return resultado != -1L
    }

    fun obtenerCantidadBombasSucursal(idSucursal: Int): Int {
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT cantidad_bombas FROM Sucursal WHERE id = ?",
            arrayOf(idSucursal.toString())
        )
        val cantidad = if (cursor.moveToFirst()) cursor.getInt(0) else 1
        cursor.close()
        return cantidad
    }

    fun obtenerUltimoResultado(): Cursor {
        val db = helper.readableDatabase
        return db.rawQuery(
            "SELECT tiempo_estimado_min, litros_restantes FROM Resultado ORDER BY id DESC LIMIT 1",
            null
        )
    }
}
