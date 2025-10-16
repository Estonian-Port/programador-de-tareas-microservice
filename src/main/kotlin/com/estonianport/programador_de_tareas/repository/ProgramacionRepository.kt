package com.estonianport.programador_de_tareas.repository

import com.estonianport.programador_de_tareas.model.Programacion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProgramacionRepository : JpaRepository<Programacion, Long> {
    fun findByJobId(jobId: String): Programacion?
    fun findByPiscinaId(piscinaId: Long): List<Programacion>
    fun findByEstado(estado: String): List<Programacion>

    @Query("SELECT p FROM Programacion p WHERE p.estado = 'ACTIVA' ORDER BY p.proximaEjecucion ASC")
    fun findProgramacionesActivas(): List<Programacion>
}