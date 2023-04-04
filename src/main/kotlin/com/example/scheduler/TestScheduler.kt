package com.example.scheduler

import org.quartz.Job
import org.quartz.JobExecutionContext

class TestScheduler:Job {
    companion object{
        var count = 0;
    }

    override fun execute(context: JobExecutionContext?) {
        count++
        println("Hello Scheduler : $count")
    }
}