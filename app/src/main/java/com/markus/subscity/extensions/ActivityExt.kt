package com.markus.subscity.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.markus.subscity.R
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.utils.IntentUtils
import com.markus.subscity.utils.analytics.Analytics

/**
 * @author Vitaliy Markus
 */
val FragmentActivity.supportActionBar: ActionBar
    get() = (this as AppCompatActivity).supportActionBar!!

val Fragment.supportActionBar: ActionBar
    get() = (this.requireActivity() as AppCompatActivity).supportActionBar!!

fun Fragment.setSupportActionBar(toolbar: Toolbar) {
    val appCompatActivity = activity as AppCompatActivity
    appCompatActivity.setSupportActionBar(toolbar)
    appCompatActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    appCompatActivity.supportActionBar!!.setDisplayShowHomeEnabled(true)
    toolbar.setNavigationOnClickListener { this.requireActivity().onBackPressed() }
}

fun Fragment.setSupportActionBarWithoutBackButton(toolbar: Toolbar) {
    val appCompatActivity = activity as AppCompatActivity
    appCompatActivity.setSupportActionBar(toolbar)
}

fun Fragment.openIntent(intent: Intent, @StringRes errorId: Int) {
    if (intent.resolveActivity(requireActivity().packageManager) != null) {
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

fun Context.getWidthScreen(): Int {
    val metrics = DisplayMetrics()
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
}

fun analytics(): Analytics {
    return SubsCityDagger.component.provideAnalytics()
}

fun Fragment.openUrl(uri: Uri, useChromeTabsForce: Boolean = true) {
    val context = requireActivity()

    val builder = CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(context, R.color.toolbar_color))
            .setStartAnimations(context, R.anim.events_right_in, R.anim.activity_open_exit)
            .setExitAnimations(context, R.anim.activity_close_enter, R.anim.events_left_out)

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
    requireActivity().rateApp()
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
