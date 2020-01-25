package com.markus.subscity.viewmodels

abstract class BaseInitableViewModel : BaseViewModel(), Initable {

    private val initOnce = InitOnce { doInit() }

    override fun init() = initOnce.init()

    protected abstract fun doInit()
}