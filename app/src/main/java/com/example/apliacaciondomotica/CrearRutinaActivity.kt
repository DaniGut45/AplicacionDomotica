package com.example.apliacaciondomotica

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class CrearRutinaActivity : AppCompatActivity() {

    private lateinit var spinnerDispositivos: Spinner
    private lateinit var switchToggle: Switch
    private lateinit var seekBarValue: SeekBar
    private lateinit var txtSeekBarValue: TextView
    private lateinit var timePicker: TimePicker
    private lateinit var btnGuardarRutina: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_rutina)

        // Vincula los elementos del XML
        spinnerDispositivos = findViewById(R.id.spinner_dispositivos)
        switchToggle = findViewById(R.id.switch_toggle)
        seekBarValue = findViewById(R.id.seekBar_value)
        txtSeekBarValue = findViewById(R.id.txt_seekbar_value)
        timePicker = findViewById(R.id.time_picker)
        btnGuardarRutina = findViewById(R.id.btn_guardar_rutina)

        spinnerDispositivos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                        seekBarValue.progress = 50 // Valor inicial
                        txtSeekBarValue.text = "Volumen: ${seekBarValue.progress}"
                    }
                    2 -> { // Luces
                        switchToggle.visibility = View.VISIBLE
                    }
                    3 -> { // Persianas
                        seekBarValue.visibility = View.VISIBLE
                        txtSeekBarValue.visibility = View.VISIBLE
                        seekBarValue.max = 100 // Cambiado a 100
                        seekBarValue.progress = 0 // Valor inicial
                        txtSeekBarValue.text = "Porcentaje: ${seekBarValue.progress}%"
                    }
                    4 -> { // Termostato
                        seekBarValue.visibility = View.VISIBLE
                        txtSeekBarValue.visibility = View.VISIBLE
                        seekBarValue.max = 50
                        seekBarValue.progress = 0 // Valor inicial
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
            val hora = timePicker.hour
            val minutos = timePicker.minute
            val dispositivo = spinnerDispositivos.selectedItem.toString()
            val estadoSwitch = switchToggle.isChecked
            val valorSeekBar = seekBarValue.progress

            val intent = Intent(this, RutinasActivity::class.java)
            intent.putExtra("dispositivo", dispositivo)
            intent.putExtra("hora", "$hora:$minutos")
            intent.putExtra("estado", estadoSwitch)
            intent.putExtra("valor", valorSeekBar)
            startActivity(intent)
        }
    }
}
