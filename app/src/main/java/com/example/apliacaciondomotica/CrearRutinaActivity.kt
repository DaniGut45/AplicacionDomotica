package com.example.apliacaciondomotica

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class CrearRutinaActivity : AppCompatActivity() {

    private lateinit var spinnerDispositivos: Spinner
    private lateinit var switchToggle: Switch
    private lateinit var seekBarValue: SeekBar
    private lateinit var txtSeekBarValue: TextView
    private lateinit var timePicker: TimePicker
    private lateinit var btnGuardarRutina: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper
    private var rutinaActualId: Int? = null

    private val ID_CANAL = "canal_notificaciones_01"
    private val REQUEST_CODE_NOTIFICATIONS = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_rutina)

        // Inicializar la base de datos
        dbHelper = DatabaseHelper(this)

        initializeViews()
        setupRecyclerView()
        setupListeners()
        crearCanalDeNotificaciones()
        // Verificar y solicitar el permiso POST_NOTIFICATIONS en tiempo de ejecución
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Comprueba si el permiso no ha sido concedido
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Solicita el permiso al usuario
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_NOTIFICATIONS)
            }
        }
    }

    private fun initializeViews() {
        spinnerDispositivos = findViewById(R.id.spinner_dispositivos)
        switchToggle = findViewById(R.id.switch_toggle)
        seekBarValue = findViewById(R.id.seekBar_value)
        txtSeekBarValue = findViewById(R.id.txt_seekbar_value)
        timePicker = findViewById(R.id.time_picker)
        btnGuardarRutina = findViewById(R.id.btn_guardar_rutina)
        recyclerView = findViewById(R.id.rvRutina)
        timePicker.setIs24HourView(true)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        refreshRecyclerView()
    }

    private fun crearCanalDeNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Definimos el nombre y la descripción del canal
            val nombre = "Canal de Rutinas"
            val descripcion = "Canal para notificaciones de rutinas"
            val importancia = NotificationManager.IMPORTANCE_HIGH

            // Creamos el canal con el ID, nombre e importancia
            val canal = NotificationChannel(ID_CANAL, nombre, importancia).apply {
                description = descripcion
            }

            // Registramos el canal en el sistema
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
    }

    /**
     * Método que se llama cuando el usuario responde a la solicitud de permisos
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_NOTIFICATIONS) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permiso concedido
            } else {
                // Permiso denegado: informa al usuario que la aplicación no podrá mostrar notificaciones
                Toast.makeText(this, "Permiso de notificaciones denegado. La aplicación no podrá mostrar notificaciones.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarDialogoPermisoAlarma() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Crea y muestra un diálogo explicando la necesidad del permiso
            AlertDialog.Builder(this)
                .setTitle("Permiso necesario")
                .setMessage("Para programar alarmas exactas, la aplicación necesita un permiso especial. Por favor, concédelo en la siguiente pantalla.")
                .setPositiveButton("Aceptar") { _, _ ->
                    // Dirige al usuario a la configuración para conceder el permiso
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    startActivity(intent)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        } else {
            // Opcional: Manejar el caso para versiones anteriores de Android
        }
    }


    private fun setupListeners() {
        spinnerDispositivos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Resetea visibilidad
                switchToggle.visibility = View.GONE
                seekBarValue.visibility = View.GONE
                txtSeekBarValue.visibility = View.GONE

                when (position) {
                    0 -> {} // Nada seleccionado
                    1 -> { // TV
                        switchToggle.visibility = View.VISIBLE
                        seekBarValue.visibility = View.VISIBLE
                        txtSeekBarValue.visibility = View.VISIBLE
                        seekBarValue.max = 100
                        seekBarValue.progress = 50
                        txtSeekBarValue.text = "Volumen: ${seekBarValue.progress}"
                    }
                    2 -> { // Luces
                        switchToggle.visibility = View.VISIBLE
                    }
                    3 -> { // Persianas
                        seekBarValue.visibility = View.VISIBLE
                        txtSeekBarValue.visibility = View.VISIBLE
                        seekBarValue.max = 100
                        seekBarValue.progress = 0
                        txtSeekBarValue.text = "Porcentaje: ${seekBarValue.progress}%"
                    }
                    4 -> { // Termostato
                        seekBarValue.visibility = View.VISIBLE
                        txtSeekBarValue.visibility = View.VISIBLE
                        seekBarValue.max = 50
                        seekBarValue.progress = 0
                        txtSeekBarValue.text = "Temperatura: ${seekBarValue.progress}°"
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        seekBarValue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (spinnerDispositivos.selectedItemPosition) {
                    1 -> txtSeekBarValue.text = "Volumen: $progress"
                    3 -> txtSeekBarValue.text = "Porcentaje: $progress%"
                    4 -> txtSeekBarValue.text = "Temperatura: $progress°"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnGuardarRutina.setOnClickListener {
            guardarRutina()

        }
    }

    private fun guardarRutina() {
        val hora = timePicker.hour
        val minutos = timePicker.minute
        val dispositivo = spinnerDispositivos.selectedItem.toString()

        val estado = when (spinnerDispositivos.selectedItemPosition) {
            1 -> if (switchToggle.isChecked) "Encendido, Volumen: ${seekBarValue.progress}" else "Apagado"
            2 -> if (switchToggle.isChecked) "Encendido" else "Apagado"
            3 -> "${seekBarValue.progress}%"
            4 -> "${seekBarValue.progress}°"
            else -> ""
        }

        val rutina = Rutina(
            id = rutinaActualId ?: 0, // Si no hay rutina en edición, id = 0 (SQLite lo genera)
            dispositivo = dispositivo,
            estado = estado,
            hora = hora,
            minutos = minutos
        )

        if (rutinaActualId == null) {
            // Nueva rutina
            val id = dbHelper.addRutina(rutina)
            if (id != -1L) {
                // Programar la notificación
                programarNotificacion(rutina)
                Toast.makeText(this, "Rutina guardada correctamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al guardar la rutina", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Modificar rutina existente
            val updated = dbHelper.updateRutina(rutina)
            if (updated) {
                Toast.makeText(this, "Rutina actualizada correctamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al actualizar la rutina", Toast.LENGTH_SHORT).show()
            }
            rutinaActualId = null // Resetear ID después de modificar
        }

        refreshRecyclerView()
        resetRutina()
    }


    @SuppressLint("ScheduleExactAlarm")
    private fun programarNotificacion(rutina: Rutina) {
        // Verifica si tienes el permiso SCHEDULE_EXACT_ALARM en Android 12 o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // Si no tienes el permiso, muestra un diálogo para solicitarlo al usuario
                mostrarDialogoPermisoAlarma()
                return
            }
        }
        // Obtengo una instancia del calendario y le fijo la hora y minutos obtenidos por parámetro
        val calendarioAlarma = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, rutina.hora)
            set(Calendar.MINUTE, rutina.minutos)
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificacionReceiver::class.java).apply {
            putExtra("dispositivo", rutina.dispositivo)
            putExtra("estado", rutina.estado)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, rutina.id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarioAlarma.timeInMillis, pendingIntent)
    }




    private fun resetRutina() {
        spinnerDispositivos.setSelection(0)
        switchToggle.visibility = View.GONE
        seekBarValue.visibility = View.GONE
        txtSeekBarValue.visibility = View.GONE
        seekBarValue.progress = 0
        txtSeekBarValue.text = ""
        rutinaActualId = null // Resetear ID
    }



    private fun getSpinnerPosition(dispositivo: String): Int {
        val dispositivos = resources.getStringArray(R.array.dispositivos_array) // Asegúrate de que este array coincide con el Spinner
        return dispositivos.indexOf(dispositivo).takeIf { it >= 0 } ?: 0
    }

    private fun refreshRecyclerView() {
        val rutinas = dbHelper.getAllRutinas()
        val adapter = RutinasAdapter(
            rutinasList = rutinas,
            onModificarClick = { rutina ->
                rutinaActualId = rutina.id // Guardar el ID de la rutina actual
                spinnerDispositivos.setSelection(getSpinnerPosition(rutina.dispositivo))
                timePicker.hour = rutina.hora
                timePicker.minute = rutina.minutos

                when (rutina.dispositivo) {
                    "TV" -> {
                        switchToggle.visibility = View.VISIBLE
                        seekBarValue.visibility = View.VISIBLE
                        txtSeekBarValue.visibility = View.VISIBLE
                        seekBarValue.max = 100
                        val volumen = rutina.estado.filter { it.isDigit() }.toIntOrNull() ?: 50
                        seekBarValue.progress = volumen
                        txtSeekBarValue.text = "Volumen: $volumen"
                    }
                    "Luces" -> {
                        switchToggle.visibility = View.VISIBLE
                        switchToggle.isChecked = rutina.estado == "Encendido"
                    }
                    "Persianas" -> {
                        seekBarValue.visibility = View.VISIBLE
                        txtSeekBarValue.visibility = View.VISIBLE
                        seekBarValue.max = 100
                        val porcentaje = rutina.estado.filter { it.isDigit() }.toIntOrNull() ?: 0
                        seekBarValue.progress = porcentaje
                        txtSeekBarValue.text = "Porcentaje: $porcentaje%"
                    }
                    "Termostato" -> {
                        seekBarValue.visibility = View.VISIBLE
                        txtSeekBarValue.visibility = View.VISIBLE
                        seekBarValue.max = 50
                        val temperatura = rutina.estado.filter { it.isDigit() }.toIntOrNull() ?: 0
                        seekBarValue.progress = temperatura
                        txtSeekBarValue.text = "Temperatura: $temperatura°"
                    }
                }
            },
            onBorrarClick = { rutina ->
                dbHelper.deleteRutina(rutina)
                refreshRecyclerView()
                Toast.makeText(this, "Rutina eliminada", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = adapter
    }

}