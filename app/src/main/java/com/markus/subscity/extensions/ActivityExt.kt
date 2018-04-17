package com.markus.subscity.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.support.annotation.StringRes
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.markus.subscity.R
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.utils.Analytics

/**
 * @author Vitaliy Markus
 */
val FragmentActivity.supportActionBar: ActionBar
    get() = (this as AppCompatActivity).supportActionBar!!

val Fragment.supportActionBar: ActionBar
    get() = (this.activity!! as AppCompatActivity).supportActionBar!!

fun Fragment.setSupportActionBar(toolbar: Toolbar) {
    val appCompatActivity = activity as AppCompatActivity
    appCompatActivity.setSupportActionBar(toolbar)
    appCompatActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    appCompatActivity.supportActionBar!!.setDisplayShowHomeEnabled(true)
    toolbar.setNavigationOnClickListener { this.activity!!.onBackPressed() }
}

fun Fragment.setSupportActionBarWithoutBackButton(toolbar: Toolbar) {
    val appCompatActivity = activity as AppCompatActivity
    appCompatActivity.setSupportActionBar(toolbar)
}

fun Fragment.openIntent(intent: Intent, @StringRes errorId: Int) {
    if (intent.resolveActivity(activity!!.packageManager) != null) {
        startActivity(intent)
    } else {
        toast(getString(errorId))
    }
}

fun Activity.openIntent(intent: Intent, @StringRes errorId: Int) {
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        toast(getString(errorId))
    }
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

fun analytics(): Analytics {
    return SubsCityDagger.component.provideAnalytics()
}

fun Fragment.openUrl(uri: Uri, useChromeTabs: Boolean = true) {
    val context = activity!!

    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(ContextCompat.getColor(context, R.color.primary_color))
    //builder.setStartAnimations(context, android.R.anim.slide_in_right, R.anim.slide_out_left);
    //builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    val customTabsIntent = builder.build()
    if (useChromeTabs) {
        val packages = getCustomTabsPackages(context, uri)
        if (packages.isNotEmpty() && (packages.size == 1 || packages[0].preferredOrder != packages[1].preferredOrder)) {
            val resolveInfo = packages[0]
            customTabsIntent.intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name)
        }
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
