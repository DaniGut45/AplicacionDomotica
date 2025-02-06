package com.example.apliacaciondomotica

data class Rutina(
    var id: Int = 0,
    var dispositivo: String = "",
    var estado: String = "",
    var hora: Int = 0,
    var minutos: Int = 0
)
