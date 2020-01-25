package com.markus.subscity.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass

abstract class ViewModelFragment<T : ViewModel>(viewModelClass: KClass<T>) : Fragment() {

    protected val viewModel: T by viewModel(viewModelClass)

    fun <T> LiveData<T>.observe(observer: (T) -> Unit) {
        observe(this@ViewModelFragment, Observer<T>(observer::invoke))
    }
}