package com.markus.subscity.viewmodels

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass

abstract class ViewModelActivity(viewModelClass: KClass<out ViewModel>) : AppCompatActivity() {

    protected val viewModel by viewModel(viewModelClass)

    fun <T> LiveData<T>.observe(observer: (T) -> Unit) {
        observe(this@ViewModelActivity, Observer<T>(observer::invoke))
    }
}