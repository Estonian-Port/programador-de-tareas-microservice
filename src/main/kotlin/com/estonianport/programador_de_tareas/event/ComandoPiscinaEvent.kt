package com.estonianport.programador_de_tareas.event

import org.springframework.context.ApplicationEvent

class ComandoPiscinaEvent(
    source: Any,
    val piscinaId: Long,
    val patente: String,
    val comando: String,
    val jobId: String,
) : ApplicationEvent(source)