package com.estonianport.programador_de_tareas.dto

data class ProgramacionRequest(
    val piscinaId: Long,
    val patente: String,
    val horaInicio: String,
    val horaFin: String,
    val dias: List<String>,
    val tipo: String,
    val jobId: String
)
