package com.example.gasolineraapplication.presentacion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.gasolineraapplication.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.SphericalUtil

class MapaDibujoActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var btnConfirmar: Button
    private var puntosDibujo: MutableList<LatLng> = mutableListOf()
    private var polyline: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_dibujo)

        btnConfirmar = findViewById(R.id.btnConfirmar)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btnConfirmar.setOnClickListener {
            if (puntosDibujo.size < 2) {
                setResult(Activity.RESULT_CANCELED)
                finish()
                return@setOnClickListener
            }

            val distancia = SphericalUtil.computeLength(puntosDibujo) // en metros
            val intent = Intent().apply {
                putExtra("metrosFila", distancia)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Posicionar cámara (por ejemplo, en Santa Cruz)
        val santaCruz = LatLng(-17.7833, -63.1821)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(santaCruz, 15f))

        googleMap.setOnMapClickListener { punto ->
            puntosDibujo.add(punto)
            actualizarLinea()
        }
    }

    private fun actualizarLinea() {
        polyline?.remove()
        polyline = googleMap.addPolyline(
            PolylineOptions()
                .addAll(puntosDibujo)
                .color(R.color.purple_500)
                .width(10f)
        )
    }
}
