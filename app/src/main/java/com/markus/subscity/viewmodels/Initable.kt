package com.markus.subscity.viewmodels

import androidx.annotation.MainThread

interface Initable {
    fun init()
}

class InitOnce(private val initMethod: () -> Unit) : Initable {

    private var inited = false

    @MainThread
    override fun init() {
        if (!inited) {
            initMethod()
            inited = true
        }
    }
}