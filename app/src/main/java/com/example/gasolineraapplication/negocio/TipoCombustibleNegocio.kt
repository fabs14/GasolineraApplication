package com.example.gasolineraapplication.negocio

import android.content.Context
import com.example.gasolineraapplication.datos.TipoCombustibleDato

class TipoCombustibleNegocio(context: Context) {
    private val tipoCombustibleDato = TipoCombustibleDato(context)

    fun listarTiposCombustible(): List<String> {
        return tipoCombustibleDato.obtenerTodos()
    }
}
