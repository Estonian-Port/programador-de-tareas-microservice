package com.estonianport.programador_de_tareas.dto

data class ProgramacionResponse(
    val jobId: String,
    val estado: String,
    val mensaje: String
)