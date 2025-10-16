package com.estonianport.programador_de_tareas.controller


import com.estonianport.programador_de_tareas.dto.ProgramacionDTO
import com.estonianport.programador_de_tareas.dto.ProgramacionRequest
import com.estonianport.programador_de_tareas.dto.ProgramacionResponse
import com.estonianport.programador_de_tareas.service.ProgramacionService
import com.estonianport.programador_de_tareas.service.QuartzSchedulerService
import com.estonianport.unique.model.enums.ProgramacionType
import org.springframework.data.annotation.CreatedBy
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.DayOfWeek
import java.time.LocalTime

@RestController
@RequestMapping("/")
@CrossOrigin("*")
class SchedulerController(
    private val quartzSchedulerService: QuartzSchedulerService,
    private val programacionService: ProgramacionService
) {

    @PostMapping("/programar")
    fun programarJob(@RequestBody request: ProgramacionRequest): ResponseEntity<ProgramacionResponse> {
        return try {
            val horaInicio = LocalTime.parse(request.horaInicio)
            val horaFin = LocalTime.parse(request.horaFin)
            val dias = request.dias.map { DayOfWeek.valueOf(it) }
            val tipo = ProgramacionType.valueOf(request.tipo)

            val comando = if (tipo == ProgramacionType.FILTRADO) "FILTRAR" else "ENCENDER_LUCES"

            quartzSchedulerService.programarJob(
                request.piscinaId,
                request.patente,
                comando,
                horaInicio,
                horaFin,
                dias,
                tipo,
                request.jobId
            )

            ResponseEntity.ok(ProgramacionResponse(
                jobId = request.jobId,
                estado = "EXITOSO",
                mensaje = "Job programado correctamente"
            ))
        } catch (e: Exception) {
            println("❌ Error en programarJob: ${e.message}")
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ProgramacionResponse(
                jobId = request.jobId,
                estado = "ERROR",
                mensaje = "Error al programar: ${e.message}"
            ))
        }
    }

    @DeleteMapping("/eliminar")
    fun eliminarJob(@RequestBody jobId: String): ResponseEntity<ProgramacionResponse> {
        return try {
            quartzSchedulerService.eliminarJob(jobId)
            ResponseEntity.ok(ProgramacionResponse(
                jobId = jobId,
                estado = "EXITOSO",
                mensaje = "Job eliminado correctamente"
            ))
        } catch (e: Exception) {
            println("❌ Error en eliminarJob: ${e.message}")
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ProgramacionResponse(
                jobId = jobId,
                estado = "ERROR",
                mensaje = "Error al eliminar: ${e.message}"
            ))
        }
    }

}