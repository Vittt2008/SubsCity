package com.source.subscity.extensions

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.source.subscity.R
import com.source.subscity.api.entities.screening.Screening

/**
 * @author Vitaliy Markus
 */
val FragmentActivity.supportActionBar: ActionBar
    get() = (this as AppCompatActivity).supportActionBar!!

fun FragmentActivity.setSupportActionBar(toolbar: Toolbar) {
    val appCompatActivity = this as AppCompatActivity
    appCompatActivity.setSupportActionBar(toolbar)
    appCompatActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    appCompatActivity.supportActionBar!!.setDisplayShowHomeEnabled(true)
    toolbar.setNavigationOnClickListener { this.onBackPressed() }
}

fun Context.toast(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(text: String?) {
    Toast.makeText(this.activity, text, Toast.LENGTH_SHORT).show()
}

fun View.toast(text: String?) {
    Toast.makeText(this.context, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.buyTicket(screening: Screening) {
    val context = activity!!
    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(ContextCompat.getColor(context, R.color.primary_color))
    //builder.setStartAnimations(context, android.R.anim.slide_in_right, R.anim.slide_out_left);
    //builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(context, Uri.parse(screening.ticketsUrl))
}
