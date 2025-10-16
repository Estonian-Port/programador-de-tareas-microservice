package com.estonianport.programador_de_tareas.job

import com.estonianport.programador_de_tareas.event.ComandoPiscinaEvent
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class EnviarComandoPiscinaJob : Job {

    override fun execute(context: JobExecutionContext) {
        val piscinaId = context.mergedJobDataMap["piscinaId"] as Long
        val patente = context.mergedJobDataMap["patente"] as String
        val comando = context.mergedJobDataMap["comando"] as String
        val jobId = context.trigger.key.name

        println("💧 Ejecutando job Quartz: $comando para piscina $piscinaId")

        try {
            val applicationContext = context.scheduler.context["applicationContext"] as ApplicationContext
            applicationContext.publishEvent(ComandoPiscinaEvent(
                this,
                piscinaId,
                patente,
                comando,
                jobId
            ))
            println("✅ Evento publicado: $comando - Plaqueta: $patente - JobId: $jobId")
        } catch (e: Exception) {
            println("❌ Error al publicar evento: ${e.message}")
            e.printStackTrace()
        }
    }
}
