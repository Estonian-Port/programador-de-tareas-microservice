package com.estonianport.programador_de_tareas.service

import com.estonianport.programador_de_tareas.job.EnviarComandoPiscinaJob
import com.estonianport.unique.model.enums.ProgramacionType
import org.quartz.*
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class QuartzSchedulerService(
    private val scheduler: Scheduler,
    private val applicationContext: org.springframework.context.ApplicationContext,
    private val programacionService: ProgramacionService
) {

    init {
        try {
            scheduler.context["applicationContext"] = applicationContext
        } catch (e: SchedulerException) {
            println("⚠️ Error al configurar ApplicationContext en Scheduler: ${e.message}")
        }
    }

    fun programarJob(
        piscinaId: Long,
        patente: String,
        comando: String,
        horaInicio: LocalTime,
        horaFin: LocalTime,
        dias: List<DayOfWeek>,
        tipo: ProgramacionType,
        jobId: String
    ) {
        dias.forEach { dia ->
            val jobDetail = JobBuilder.newJob(EnviarComandoPiscinaJob::class.java)
                .withIdentity("${jobId}_${dia.name}")
                .usingJobData("piscinaId", piscinaId)
                .usingJobData("patente", patente)
                .usingJobData("comando", comando)
                .usingJobData("jobId", jobId)
                .build()

            val quartzDayOfWeek = diasemanaAQuartz(dia)

            val trigger = TriggerBuilder.newTrigger()
                .withIdentity("${jobId}_${dia.name}_trigger")
                .withSchedule(
                    CronScheduleBuilder.weeklyOnDayAndHourAndMinute(
                        quartzDayOfWeek,
                        horaInicio.hour,
                        horaInicio.minute
                    )
                )
                .build()

            scheduler.scheduleJob(jobDetail, trigger)
        }

        // Guardar en BD
        programacionService.guardarProgramacion(
            piscinaId,
            patente,
            comando,
            horaInicio,
            horaFin,
            dias,
            tipo,
            jobId
        )

        println("🕒 Quartz Job programado -> $jobId ($comando para días: ${dias.map { it.name }} desde ${horaInicio})")
    }

    fun eliminarJob(jobId: String) {
        val tarea = programacionService.obtenerProgramacionPorJobId(jobId)
        if (tarea != null) {
            tarea.dias.forEach { dia ->
                val jobKey = JobKey.jobKey("${jobId}_${dia.name}")
                if (scheduler.checkExists(jobKey)) {
                    scheduler.deleteJob(jobKey)
                }
            }
            // Eliminar de BD
            programacionService.eliminarProgramacion(jobId)
            println("🗑️ Quartz Job eliminado -> $jobId")
        }
    }

    fun calcularProximaEjecucion(dias: List<DayOfWeek>, hora: LocalTime): LocalDateTime {
        return programacionService.calcularProximaEjecucion(dias, hora)
    }

    private fun diasemanaAQuartz(diaSemana: DayOfWeek): Int {
        return when (diaSemana) {
            DayOfWeek.MONDAY -> 2
            DayOfWeek.TUESDAY -> 3
            DayOfWeek.WEDNESDAY -> 4
            DayOfWeek.THURSDAY -> 5
            DayOfWeek.FRIDAY -> 6
            DayOfWeek.SATURDAY -> 7
            DayOfWeek.SUNDAY -> 1
        }
    }
}
