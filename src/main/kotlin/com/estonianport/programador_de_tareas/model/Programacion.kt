package com.estonianport.programador_de_tareas.model

import com.estonianport.unique.model.enums.ProgramacionType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

class Programacion(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var piscinaId: Long,

    @Column(nullable = false)
    var patente: String,

    @Column(nullable = false)
    var comando: String,

    @Column(nullable = false)
    var horaInicio: LocalTime,

    @Column(nullable = false)
    var horaFin: LocalTime,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "tarea_dias",
        joinColumns = [JoinColumn(name = "tarea_id")]
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "dia")
    var dias: MutableList<DayOfWeek> = mutableListOf(),

    @Column(nullable = false)
    var jobId: String,

    @Column(nullable = false)
    var estado: String = "ACTIVA", // ACTIVA, PAUSADA, EJECUTADA, ERROR

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var tipo: ProgramacionType,

    @Column(nullable = false)
    var fechaCreacion: LocalDateTime = LocalDateTime.now(),

    @Column
    var fechaUltimaEjecucion: LocalDateTime? = null,

    @Column
    var proximaEjecucion: LocalDateTime? = null,

    @Column
    var descripcionError: String? = null,

    @Column(nullable = false)
    var activa: Boolean = true
)
