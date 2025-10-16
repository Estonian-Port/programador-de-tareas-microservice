package com.estonianport.programador_de_tareas.common.mqtt

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MqttConfig(private val mqttProperties: MqttProperties) {

    fun buildConnectionOptions(): MqttConnectOptions {
        return MqttConnectOptions().apply {
            isCleanSession = true
            userName = mqttProperties.username
            password = mqttProperties.password.toCharArray()
        }
    }

    @Bean
    fun mqttClient(): MqttClient {
        val broker = mqttProperties.brokerUrl
        val clientId = "programador-tareas-${System.currentTimeMillis()}"
        val persistence = MemoryPersistence()
        val client = MqttClient(broker, clientId, persistence)

        val connOpts = buildConnectionOptions()

        client.connect(connOpts)
        println("✅ Programador de Tareas conectado al broker MQTT")
        return client
    }
}