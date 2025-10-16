package com.estonianport.programador_de_tareas.service

import com.estonianport.programador_de_tareas.model.Programacion
import com.estonianport.programador_de_tareas.repository.ProgramacionRepository
import com.estonianport.unique.model.enums.ProgramacionType
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

@Service
class ProgramacionService(
    private val programacionRepository: ProgramacionRepository
) {

    fun guardarProgramacion(
        piscinaId: Long,
        patente: String,
        comando: String,
        horaInicio: LocalTime,
        horaFin: LocalTime,
        dias: List<DayOfWeek>,
        tipo: ProgramacionType,
        jobId: String
    ): Programacion {
        val proximaEjecucion = calcularProximaEjecucion(dias, horaInicio)

        val programacion = Programacion(
            piscinaId = piscinaId,
            patente = patente,
            comando = comando,
            horaInicio = horaInicio,
            horaFin = horaFin,
            dias = dias.toMutableList(),
            tipo = tipo,
            jobId = jobId,
            estado = "ACTIVA",
            proximaEjecucion = proximaEjecucion,
            activa = true
        )

        return programacionRepository.save(programacion)
    }

    fun obtenerProgramacionPorJobId(jobId: String): Programacion? {
        return programacionRepository.findByJobId(jobId)
    }

    fun eliminarProgramacion(jobId: String) {
        val programacion = programacionRepository.findByJobId(jobId) ?: return
        programacionRepository.delete(programacion)
        println("🗑️ Programacion eliminada: $jobId")
    }

    fun calcularProximaEjecucion(dias: List<DayOfWeek>, hora: LocalTime): LocalDateTime {
        val ahora = LocalDateTime.now()
        var proxima: LocalDateTime? = null

        for (dia in dias) {
            var candidato = ahora.with(TemporalAdjusters.nextOrSame(dia))
                .withHour(hora.hour)
                .withMinute(hora.minute)
                .withSecond(0)
                .withNano(0)

            if (candidato.isBefore(ahora)) {
                candidato = candidato.plusWeeks(1)
            }

            if (proxima == null || candidato.isBefore(proxima)) {
                proxima = candidato
            }
        }

        return proxima ?: ahora.plusDays(1)
    }
}
