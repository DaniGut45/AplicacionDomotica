package com.example.apliacaciondomotica

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "RutinasDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_RUTINAS = "rutinas"

        private const val KEY_ID = "id"
        private const val KEY_DISPOSITIVO = "dispositivo"
        private const val KEY_ESTADO = "estado"
        private const val KEY_HORA = "hora"
        private const val KEY_MINUTOS = "minutos"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createRutinasTable = ("CREATE TABLE " + TABLE_RUTINAS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DISPOSITIVO + " TEXT,"
                + KEY_ESTADO + " TEXT,"
                + KEY_HORA + " INTEGER,"
                + KEY_MINUTOS + " INTEGER" + ")")
        db.execSQL(createRutinasTable)
    }

    //Solo si cambia el DATABASE_VERSION
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RUTINAS")
        onCreate(db)
    }

    fun getAllRutinas(): ArrayList<Rutina> {
        val rutinasList = ArrayList<Rutina>()
        val selectQuery = "SELECT * FROM $TABLE_RUTINAS"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            Log.e("DatabaseHelper", "Error al obtener rutinas", e)
            return ArrayList()
        }

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val idIndex = it.getColumnIndex(KEY_ID)
                    val dispositivoIndex = it.getColumnIndex(KEY_DISPOSITIVO)
                    val estadoIndex = it.getColumnIndex(KEY_ESTADO)
                    val horaIndex = it.getColumnIndex(KEY_HORA)
                    val minutosIndex = it.getColumnIndex(KEY_MINUTOS)

                    if (idIndex != -1 && dispositivoIndex != -1 && estadoIndex != -1
                        && horaIndex != -1 && minutosIndex != -1) {

                        val rutina = Rutina(
                            id = it.getInt(idIndex),
                            dispositivo = it.getString(dispositivoIndex),
                            estado = it.getString(estadoIndex),
                            hora = it.getInt(horaIndex),
                            minutos = it.getInt(minutosIndex)
                        )
                        rutinasList.add(rutina)
                    }
                } while (it.moveToNext())
            }
        }

        return rutinasList
    }

    fun addRutina(rutina: Rutina): Long {
        try {
            val db = this.writableDatabase
            val contentValues = ContentValues().apply {
                put(KEY_DISPOSITIVO, rutina.dispositivo)
                put(KEY_ESTADO, rutina.estado)
                put(KEY_HORA, rutina.hora)
                put(KEY_MINUTOS, rutina.minutos)
            }

            val success = db.insert(TABLE_RUTINAS, null, contentValues)
            db.close()
            return success
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error al agregar rutina", e)
            return -1
        }
    }

    fun updateRutina(rutina: Rutina): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("dispositivo", rutina.dispositivo)
            put("estado", rutina.estado)
            put("hora", rutina.hora)
            put("minutos", rutina.minutos)
        }
        val rowsUpdated = db.update("rutinas", values, "id = ?", arrayOf(rutina.id.toString()))
        db.close()
        return rowsUpdated > 0
    }


    fun deleteRutina(rutina: Rutina): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_RUTINAS,
            "$KEY_ID = ?", arrayOf(rutina.id.toString()))
        db.close()
        return success
    }
}