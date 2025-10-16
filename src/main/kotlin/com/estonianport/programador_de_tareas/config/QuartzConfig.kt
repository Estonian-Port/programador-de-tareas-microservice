package com.estonianport.programador_de_tareas.config

import org.springframework.context.annotation.Bean
import org.springframework.scheduling.quartz.SchedulerFactoryBean as SpringSchedulerFactoryBean

@Bean
fun schedulerFactoryBean(): SpringSchedulerFactoryBean {
    val schedulerFactoryBean = SpringSchedulerFactoryBean()
    schedulerFactoryBean.setQuartzProperties(quartzProperties())
    schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true)
    schedulerFactoryBean.setOverwriteExistingJobs(true)
    return schedulerFactoryBean
}

private fun quartzProperties(): java.util.Properties {
    val properties = java.util.Properties()
    properties["org.quartz.scheduler.instanceName"] = "ProgramadorTareasScheduler"
    properties["org.quartz.threadPool.threadCount"] = "10"
    properties["org.quartz.jobStore.class"] = "org.quartz.simpl.RAMJobStore"
    return properties
}