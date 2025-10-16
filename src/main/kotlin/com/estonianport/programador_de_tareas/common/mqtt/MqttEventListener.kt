package com.estonianport.programador_de_tareas.common.mqtt

import com.estonianport.programador_de_tareas.event.ComandoPiscinaEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class MqttEventListener(
    private val mqttPublisherService: MqttPublisherService
) {

    @EventListener
    fun handleComandoPiscina(event: ComandoPiscinaEvent) {
        try {
            println("🎯 Procesando evento: ${event.comando} para piscina ${event.piscinaId}")

            mqttPublisherService.sendCommand(event.patente, event.comando)

            println("✅ Comando MQTT enviado exitosamente: ${event.comando}")
        } catch (e: Exception) {
            println("❌ Error al enviar comando MQTT: ${e.message}")
            e.printStackTrace()
        }
    }
}