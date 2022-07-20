package com.example.superrestoration_client.utils

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ThreadManager {
    private val CPU_NUM = Runtime.getRuntime().availableProcessors()
    private const val CPU_Efficiency = 1.0
    private const val CPU_Calculate = 0.5
    private const val CPU_Wait = 0.5
    private val MAX_THREAD =
        (CPU_NUM * CPU_Efficiency * (CPU_Calculate + CPU_Wait) / CPU_Calculate).toInt()
    private val fixedThreadService = Executors.newFixedThreadPool(MAX_THREAD)
    fun getFixedThreadPool(): ExecutorService {
        return fixedThreadService
    }
}