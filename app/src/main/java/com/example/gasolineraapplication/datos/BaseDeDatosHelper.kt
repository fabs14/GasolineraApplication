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
                altitud REAL,
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
                fechahora_calculo TEXT DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (id_sucursalcombustible) REFERENCES SucursalCombustible(id) ON DELETE CASCADE
            );
        """.trimIndent())

        // Insertar datos predefinidos
        insertarDatosIniciales(db)
    }

    private fun insertarDatosIniciales(db: SQLiteDatabase) {
        // Insertar sucursales
        db.execSQL("""
            INSERT INTO Sucursal (nombre, direccion, altitud, longitud, cantidad_bombas) VALUES
            ('Sucursal Norte', 'Av. Banzer Km 6', -17.7350, -63.1350, 4),
            ('Sucursal Centro', 'Calle Libertad N° 123', -17.7833, -63.1820, 3),
            ('Sucursal Sur', '4to Anillo y Av. Santos Dumont', -17.8166, -63.1700, 5);
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
            (1, 1), -- Norte - Gasolina
            (1, 2), -- Norte - Diésel
            (2, 1), -- Centro - Gasolina
            (2, 3), -- Centro - Etanol
            (3, 2), -- Sur - Diésel
            (3, 3); -- Sur - Etanol
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
