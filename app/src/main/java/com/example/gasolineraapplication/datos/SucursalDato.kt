package com.example.gasolineraapplication.datos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.gasolinera.gasolineraapplication.datos.BaseDeDatosHelper

class SucursalDato(context: Context) {
    private val helper = BaseDeDatosHelper(context)
    private lateinit var db: SQLiteDatabase

    // Devuelve lista de sucursales que tienen el tipo de combustible dado
    fun obtenerSucursalesPorTipo(nombreCombustible: String): List<Pair<Int, String>> {
        val lista = mutableListOf<Pair<Int, String>>()
        db = helper.readableDatabase

        val query = """
            SELECT s.id, s.nombre 
            FROM Sucursal s
            JOIN SucursalCombustible sc ON s.id = sc.id_sucursal
            JOIN TipoCombustible tc ON sc.id_combustible = tc.id
            WHERE tc.nombre = ?
            GROUP BY s.id, s.nombre
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(nombreCombustible))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                lista.add(Pair(id, nombre))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    fun obtenerIdSucursalCombustible(idSucursal: Int, nombreCombustible: String): Int {
        db = helper.readableDatabase

        val query = """
        SELECT sc.id
        FROM SucursalCombustible sc
        JOIN TipoCombustible tc ON sc.id_combustible = tc.id
        WHERE sc.id_sucursal = ? AND tc.nombre = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(idSucursal.toString(), nombreCombustible))
        var id = -1

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0)
        }

        cursor.close()
        db.close()
        return id
    }


}
