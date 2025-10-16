package com.estonianport.programador_de_tareas.common.mqtt

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.springframework.stereotype.Service

@Service
class MqttPublisherService(private val mqttClient: MqttClient) {

    fun sendCommand(patente: String, comando: String) {
        val topic = "plaquetas/comando"
        val idSolicitud = System.currentTimeMillis()
        val payload = """{
            "patente": "$patente",
            "id_solicitud": $idSolicitud,
            "accion": "$comando"
        }"""

        val message = MqttMessage(payload.toByteArray()).apply { qos = 1 }

        try {
            mqttClient.publish(topic, message)
            println("📨 Comando MQTT publicado: $comando para plaqueta $patente")
        } catch (e: Exception) {
            println("❌ Error al publicar comando MQTT: ${e.message}")
            throw e
        }
    }
}
