package com.source.subscity.extensions

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast

/**
 * @author Vitaliy Markus
 */
val FragmentActivity.supportActionBar: ActionBar
    get() = (this as AppCompatActivity).supportActionBar!!

fun FragmentActivity.setSupportActionBar(toolbar: Toolbar) {
    (this as AppCompatActivity).setSupportActionBar(toolbar)
}

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(text: String) {
    Toast.makeText(this.activity, text, Toast.LENGTH_SHORT).show()
}

fun View.toast(text: String) {
    Toast.makeText(this.context, text, Toast.LENGTH_SHORT).show()
}