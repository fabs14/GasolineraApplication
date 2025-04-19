package com.gasolinera.gasolineraapplication.datos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDeDatosHelper(context: Context) : SQLiteOpenHelper(
    context,
    "GasolineraDB.db",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase) {
        // Crear tablas
        db.execSQL("""
            CREATE TABLE Sucursal (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                direccion TEXT,
                latitud REAL,
                longitud REAL,
                cantidad_bombas INTEGER NOT NULL
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE TipoCombustible (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE SucursalCombustible (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_sucursal INTEGER NOT NULL,
                id_combustible INTEGER NOT NULL,
                FOREIGN KEY (id_sucursal) REFERENCES Sucursal(id) ON DELETE CASCADE,
                FOREIGN KEY (id_combustible) REFERENCES TipoCombustible(id) ON DELETE CASCADE
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE Resultado (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_sucursalcombustible INTEGER NOT NULL,
                litros_ingresados REAL NOT NULL,
                metros_fila REAL NOT NULL,
                tiempo_estimado_min INTEGER NOT NULL,
                litros_restantes REAL NOT NULL,
                alcanza_combustible INTEGER NOT NULL, -- 👈 NUEVO CAMPO BOOLEANO (0 o 1)
                fechahora_calculo TEXT DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (id_sucursalcombustible) REFERENCES SucursalCombustible(id) ON DELETE CASCADE
            );
        """.trimIndent())

        insertarDatosIniciales(db)
    }

    private fun insertarDatosIniciales(db: SQLiteDatabase) {
        // Insertar sucursales
        db.execSQL("""
            INSERT INTO Sucursal (nombre, direccion, latitud, longitud, cantidad_bombas) VALUES
            ('Sucursal Alemana', 'Av. Alemana 2do Anillo', -17.769060723710524, -63.170995768184376, 4),
            ('Sucursal Sur', 'Av. Santos Dumont 2do Anillo', -17.799920809954177, -63.18045380040208, 3),
            ('Sucursal Beni', 'Av. Beni 2do Anillo', -17.769445762774943, -63.17875036029484, 5);
        """.trimIndent())

        // Insertar tipos de combustible
        db.execSQL("""
            INSERT INTO TipoCombustible (nombre) VALUES
            ('Gasolina Especial'),
            ('Diésel'),
            ('Etanol');
        """.trimIndent())

        // Insertar asociaciones sucursal-combustible
        db.execSQL("""
            INSERT INTO SucursalCombustible (id_sucursal, id_combustible) VALUES
            (1, 1),  -- Alemana Gasolina
            (1, 2),  -- Alemana Diesel
            (2, 1),  -- Sur Gasolina
            (2, 3),  -- Sur Etanol
            (3, 2),  -- Beni Diesel
            (3, 3);  -- Beni Etanol
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Resultado")
        db.execSQL("DROP TABLE IF EXISTS SucursalCombustible")
        db.execSQL("DROP TABLE IF EXISTS TipoCombustible")
        db.execSQL("DROP TABLE IF EXISTS Sucursal")
        onCreate(db)
    }
}
