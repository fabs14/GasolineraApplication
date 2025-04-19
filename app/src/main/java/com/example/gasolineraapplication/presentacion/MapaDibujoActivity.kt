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
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.SphericalUtil

class MapaDibujoActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var btnConfirmar: Button
    private var puntosDibujo: MutableList<LatLng> = mutableListOf()
    private var polyline: Polyline? = null

    // Variables para la ubicación de la sucursal
    private var latitudSucursal = -1.0
    private var longitudSucursal = -1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_dibujo)

        btnConfirmar = findViewById(R.id.btnConfirmar)

        // Obtener latitud y longitud de la sucursal desde el Intent
        latitudSucursal = intent.getDoubleExtra("latitud", -1.0)
        longitudSucursal = intent.getDoubleExtra("longitud", -1.0)

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

        // Centrar en la sucursal si hay ubicación válida
        if (latitudSucursal != -1.0 && longitudSucursal != -1.0) {
            val ubicacionSucursal = LatLng(latitudSucursal, longitudSucursal)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionSucursal, 17f))
            googleMap.addMarker(MarkerOptions().position(ubicacionSucursal).title("Sucursal"))
        } else {
            // Si no se pasó ubicación, centrar en Santa Cruz como antes
            val santaCruz = LatLng(-17.7833, -63.1821)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(santaCruz, 15f))
        }

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
