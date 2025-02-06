package com.example.apliacaciondomotica

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RutinasAdapter(
    private val rutinasList: List<Rutina>,
    private val onModificarClick: (Rutina) -> Unit,
    private val onBorrarClick: (Rutina) -> Unit
) : RecyclerView.Adapter<RutinasAdapter.RutinaViewHolder>() {

    class RutinaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtRutinaInfo: TextView = view.findViewById(R.id.txt_rutina_info)
        val btnModificar: Button = view.findViewById(R.id.btn_modificar)
        val btnBorrar: Button = view.findViewById(R.id.btn_borrar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.rutina_item, parent, false)
        return RutinaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RutinaViewHolder, position: Int) {
        val rutina = rutinasList[position]

        // Formatear la información de la rutina
        val infoText = "Dispositivo: ${rutina.dispositivo}\n" +
                "Estado: ${rutina.estado}\n" +
                "Hora: ${String.format("%02d:%02d", rutina.hora, rutina.minutos)}"

        holder.txtRutinaInfo.text = infoText

        // Configurar listeners para los botones
        holder.btnModificar.setOnClickListener {
            onModificarClick(rutina)
        }

        holder.btnBorrar.setOnClickListener {
            onBorrarClick(rutina)
        }
    }

    override fun getItemCount() = rutinasList.size
}