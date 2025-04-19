package com.example.gasolineraapplication.presentacion

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.gasolineraapplication.R
import com.google.android.material.navigation.NavigationView
import android.view.LayoutInflater

open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    protected lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun setContentView(layoutResID: Int) {
        // Inflar el layout base con drawer y toolbar
        val drawer = LayoutInflater.from(this).inflate(R.layout.base_drawer_layout, null) as DrawerLayout
        val container = drawer.findViewById<ViewGroup>(R.id.content_frame)
        LayoutInflater.from(this).inflate(layoutResID, container, true)
        super.setContentView(drawer)

        drawerLayout = drawer
        navigationView = drawer.findViewById(R.id.navigationView)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setTitleTextAppearance(this, R.style.ToolbarTitleStyle)

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_abrir,
            R.string.drawer_cerrar
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_tipo_combustible -> {
                if (this !is TipoCombustiblePresentacion) {
                    Toast.makeText(this, "Ir a CALCULAR", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, TipoCombustiblePresentacion::class.java))
                    finish()
                }
            }
            R.id.nav_historial -> {
                if (this !is HistorialPresentacion) {
                    Toast.makeText(this, "Ir a HISTORIAL", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HistorialPresentacion::class.java))
                    finish()
                }
            }
            else -> {
                Toast.makeText(this, "Opción no válida", Toast.LENGTH_SHORT).show()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
