package com.example.gasolineraapplication.datos

import android.content.Context
import android.database.Cursor
import com.gasolinera.gasolineraapplication.datos.BaseDeDatosHelper

class HistorialDato(context: Context) {

    private val helper = BaseDeDatosHelper(context)

    fun obtenerHistorial(): Cursor {
        val db = helper.readableDatabase
        val sql = """
            SELECT 
                R.fechahora_calculo, 
                R.tiempo_estimado_min, 
                R.litros_restantes, 
                TC.nombre AS tipo, 
                S.nombre AS sucursal,
                R.alcanza_combustible 
            FROM Resultado R
            JOIN SucursalCombustible SC ON R.id_sucursalcombustible = SC.id
            JOIN Sucursal S ON SC.id_sucursal = S.id
            JOIN TipoCombustible TC ON SC.id_combustible = TC.id
            ORDER BY R.fechahora_calculo DESC
        """
        return db.rawQuery(sql, null)
    }
}
