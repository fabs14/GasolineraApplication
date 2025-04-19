package com.example.gasolineraapplication.datos


import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.gasolinera.gasolineraapplication.datos.BaseDeDatosHelper


class TipoCombustibleDato(context: Context) {
    private val helper = BaseDeDatosHelper(context)
    private lateinit var db: SQLiteDatabase

    fun obtenerTodos(): List<String> {
        val lista = mutableListOf<String>()
        db = helper.readableDatabase

        val cursor: Cursor = db.rawQuery("SELECT nombre FROM TipoCombustible", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }
}
