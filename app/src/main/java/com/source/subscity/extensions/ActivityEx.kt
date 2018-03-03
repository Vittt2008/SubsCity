package com.source.subscity.extensions

import android.support.v4.app.FragmentActivity
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity

/**
 * @author Vitaliy Markus
 */
val FragmentActivity.supportActionBar: ActionBar
    get() = (this as AppCompatActivity).supportActionBar!!