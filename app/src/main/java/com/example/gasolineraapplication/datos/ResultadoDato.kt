package com.example.gasolineraapplication.datos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.gasolinera.gasolineraapplication.datos.BaseDeDatosHelper
import java.text.SimpleDateFormat
import java.util.*

class ResultadoDato(context: Context) {

    private val helper = BaseDeDatosHelper(context)

    fun insertarResultado(
        idSucursalCombustible: Int,
        litrosIngresados: Double,
        metrosFila: Double,
        tiempoEstimadoMin: Int,
        litrosRestantes: Double,
        alcanza: Boolean // NUEVO parámetro booleano
    ): Boolean {
        val db = helper.writableDatabase

        // Validar duplicado dentro de 60 segundos
        val checkSql = """
            SELECT COUNT(*) FROM Resultado 
            WHERE id_sucursalcombustible = ? AND litros_ingresados = ? AND metros_fila = ?
            AND ABS(strftime('%s','now') - strftime('%s',fechahora_calculo)) < 60
        """.trimIndent()

        val cursor = db.rawQuery(
            checkSql,
            arrayOf(
                idSucursalCombustible.toString(),
                litrosIngresados.toString(),
                metrosFila.toString()
            )
        )

        cursor.moveToFirst()
        val existe = cursor.getInt(0) > 0
        cursor.close()

        if (existe) return false

        // Formatear fecha y hora actual
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val fechaHoraActual = sdf.format(Date())

        // Insertar nuevo resultado
        val valores = ContentValues().apply {
            put("id_sucursalcombustible", idSucursalCombustible)
            put("litros_ingresados", litrosIngresados)
            put("metros_fila", metrosFila)
            put("tiempo_estimado_min", tiempoEstimadoMin)
            put("litros_restantes", litrosRestantes)
            put("alcanza_combustible", if (alcanza) 1 else 0) // Se guarda como 1 o 0
            put("fechahora_calculo", fechaHoraActual)
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

    // ACTUALIZADO: ahora devuelve también el campo alcanza_combustible
    fun obtenerUltimoResultado(idSucursalCombustible: Int): Cursor {
        val db = helper.readableDatabase
        return db.rawQuery(
            """
                SELECT tiempo_estimado_min, litros_restantes, alcanza_combustible
                FROM Resultado
                WHERE id_sucursalcombustible = ?
                ORDER BY id DESC LIMIT 1
            """.trimIndent(),
            arrayOf(idSucursalCombustible.toString())
        )
    }

    fun obtenerUbicacionSucursal(idSucursal: Int): Pair<Double, Double>? {
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT latitud, longitud FROM Sucursal WHERE id = ?",
            arrayOf(idSucursal.toString())
        )

        val ubicacion = if (cursor.moveToFirst()) {
            val latitud = cursor.getDouble(0)
            val longitud = cursor.getDouble(1)
            Pair(latitud, longitud)
        } else null

        cursor.close()
        return ubicacion
    }
}
