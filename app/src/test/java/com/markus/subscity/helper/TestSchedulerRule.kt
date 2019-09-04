package com.markus.subscity.helper

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * @author Vitaliy Markus
 */
class TestSchedulerRule : TestRule {

    companion object {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker = ExecutorScheduler.ExecutorWorker({ runnable -> runnable.run() }, true)
        }
    }

    val mainThreadScheduler: Scheduler = immediate
    val singleScheduler: Scheduler = immediate
    val newThreadScheduler: Scheduler = immediate
    val computationScheduler: TestScheduler = TestScheduler()
    val ioScheduler: Scheduler = immediate

    override fun apply(base: Statement, d: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { ioScheduler }
                RxJavaPlugins.setComputationSchedulerHandler { computationScheduler }
                RxJavaPlugins.setNewThreadSchedulerHandler { newThreadScheduler }
                RxJavaPlugins.setSingleSchedulerHandler { singleScheduler }
                RxAndroidPlugins.setMainThreadSchedulerHandler { mainThreadScheduler }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { mainThreadScheduler }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}