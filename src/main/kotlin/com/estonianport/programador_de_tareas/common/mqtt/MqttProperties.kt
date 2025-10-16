package com.estonianport.programador_de_tareas.common.mqtt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "mqtt")
class MqttProperties {
    lateinit var brokerUrl: String
    lateinit var username: String
    lateinit var password: String
}
