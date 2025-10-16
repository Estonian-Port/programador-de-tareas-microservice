package com.estonianport.programador_de_tareas.dto

data class ProgramacionDTO(
    val id: Long?,
    val piscinaId: Long,
    val patente: String,
    val comando: String,
    val horaInicio: String,
    val horaFin: String,
    val dias: List<String>,
    val tipo: String,
    val jobId: String,
    val estado: String,
    val activa: Boolean,
    val fechaCreacion: String,
    val fechaUltimaEjecucion: String?,
    val proximaEjecucion: String?,
    val descripcionError: String?
)