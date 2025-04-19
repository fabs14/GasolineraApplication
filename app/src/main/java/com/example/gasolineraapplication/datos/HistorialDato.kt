package com.example.gasolineraapplication.datos

import android.content.Context
import android.database.Cursor
import com.gasolinera.gasolineraapplication.datos.BaseDeDatosHelper

class HistorialDato(context: Context) {

    private val helper = BaseDeDatosHelper(context)

    fun obtenerHistorial(): Cursor {
        val db = helper.readableDatabase
        val consulta = """
            SELECT 
                Resultado.fechahora_calculo,
                Resultado.tiempo_estimado_min,
                Resultado.litros_restantes,
                TipoCombustible.nombre AS tipo,
                Sucursal.nombre AS sucursal
            FROM Resultado
            INNER JOIN SucursalCombustible ON Resultado.id_sucursalcombustible = SucursalCombustible.id
            INNER JOIN TipoCombustible ON SucursalCombustible.id_combustible = TipoCombustible.id
            INNER JOIN Sucursal ON SucursalCombustible.id_sucursal = Sucursal.id
            ORDER BY Resultado.fechahora_calculo DESC
        """.trimIndent()
        return db.rawQuery(consulta, null)
    }
}
