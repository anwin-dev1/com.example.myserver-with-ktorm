package com.example.scheduler

import org.quartz.*
import org.quartz.JobBuilder
import org.quartz.JobDataMap
import org.quartz.Scheduler
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory

class Scheduler {
    private val scheduler: Scheduler = StdSchedulerFactory().scheduler

    fun scheduleJob(
        jobName: String,
        jobGroup: String,
        triggerName: String,
        triggerGroup: String,
        cronExpression: String,
        jobData: JobDataMap,
        jobClass: Class<out Job>
    ) {
        val job = JobBuilder.newJob(jobClass)
            .withIdentity(jobName, jobGroup)
            .usingJobData(jobData)
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity(triggerName, triggerGroup)
            .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(3))
//            .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
            .build()

        scheduler.scheduleJob(job, trigger)
    }

    fun start() {
        scheduler.start()
    }

    fun shutdown(waitForJobsToComplete: Boolean = true) {
        scheduler.shutdown(waitForJobsToComplete)
    }
}