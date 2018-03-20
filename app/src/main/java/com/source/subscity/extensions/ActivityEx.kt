package com.source.subscity.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
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

fun Fragment.openUrl(uri: Uri) {
    val context = activity!!

    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(ContextCompat.getColor(context, R.color.primary_color))
    //builder.setStartAnimations(context, android.R.anim.slide_in_right, R.anim.slide_out_left);
    //builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    val customTabsIntent = builder.build()
    val packages = getCustomTabsPackages(context, uri)
    if (packages.isNotEmpty() && (packages.size == 1 || packages[0].preferredOrder != packages[1].preferredOrder)) {
        val resolveInfo = packages[0]
        customTabsIntent.intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name)
    }
    customTabsIntent.launchUrl(context, uri)
}

private const val ACTION_CUSTOM_TABS_CONNECTION = "android.support.customtabs.action.CustomTabsService"

private fun getCustomTabsPackages(context: Context, uri: Uri): List<ResolveInfo> {
    val pm = context.packageManager
    val activityIntent = Intent(Intent.ACTION_VIEW, uri)

    val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
    val packagesSupportingCustomTabs = resolvedActivityList.filter { info ->
        val serviceIntent = Intent()
        serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
        serviceIntent.setPackage(info.activityInfo.packageName)
        return@filter pm.resolveService(serviceIntent, 0) != null
    }.sortedBy { it.preferredOrder }

    return packagesSupportingCustomTabs
}
