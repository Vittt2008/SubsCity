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
import android.widget.Toast
import com.markus.subscity.R
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.utils.Analytics
import com.markus.subscity.utils.IntentUtils

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

fun Context.longToast(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun analytics(): Analytics {
    return SubsCityDagger.component.provideAnalytics()
}

fun Fragment.openUrl(uri: Uri, useChromeTabsForce: Boolean = true) {
    val context = activity!!

    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(ContextCompat.getColor(context, R.color.primary_color))
    //builder.setStartAnimations(context, android.R.anim.slide_in_right, R.anim.slide_out_left)
//    builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)

    builder.setStartAnimations(context, R.anim.events_right_in, R.anim.activity_open_exit)
    builder.setExitAnimations(context, R.anim.activity_close_enter, R.anim.events_left_out)
//    builder.setStartAnimations(context, R.anim.events_right_in, R.anim.activity_close_exit)
//    builder.setExitAnimations(context, R.anim.activity_close_enter, R.anim.activity_open_exit)


    val customTabsIntent = builder.build()
    if (useChromeTabsForce) {
        val packages = getCustomTabsPackages(context, uri)
        if (packages.isNotEmpty()) {
            val resolveInfo = packages[0]
            customTabsIntent.intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name)
        }
    }
    customTabsIntent.launchUrl(context, uri)
}

fun Activity.rateApp() {
    val intent = IntentUtils.createOpenPlayStoreIntent(this)
    if (intent.resolveActivity(this.packageManager) != null) {
        analytics().logOpenPlayStore(true)
        startActivity(intent)
    } else {
        val email = getString(R.string.email_address)
        val emailIntent = IntentUtils.createSendEmailIntent(email, getString(R.string.email_rate_app))
        analytics().logOpenEmail(email)
        openIntent(emailIntent, R.string.about_no_email_application)
    }
}

fun Fragment.rateApp() {
    activity!!.rateApp()
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
