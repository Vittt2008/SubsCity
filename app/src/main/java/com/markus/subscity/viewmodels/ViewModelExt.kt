package com.markus.subscity.viewmodels

import androidx.lifecycle.ViewModel
import com.markus.subscity.dagger.SubsCityDagger
import kotlin.reflect.KClass

fun <T : ViewModel> viewModel(viewModelClass: KClass<T>): Lazy<T> {
    return lazy {
        val viewModelProvider = SubsCityDagger.component.getViewModelProvider()
        viewModelProvider.get(viewModelClass.java)
    }
}