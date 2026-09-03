package com.darrenai.jarvis.ui.schedule

data class CronJob(
    val id: String,
    val name: String,
    val schedule: String,
    var status: String,
    val lastRun: String
)
