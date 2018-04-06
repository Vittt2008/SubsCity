package com.source.subscity.ui.about

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.source.subscity.R
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.extensions.openUrl
import com.source.subscity.extensions.setSupportActionBar
import com.source.subscity.extensions.toast
import com.source.subscity.utils.IntentUtils

/**
 * @author Vitaliy Markus
 */
class AboutFragment : MvpAppCompatFragment(), AboutView {

    @InjectPresenter
    lateinit var aboutPresenter: AboutPresenter

    companion object {
        fun newInstance(): AboutFragment {
            return AboutFragment()
        }
    }

    @ProvidePresenter
    fun aboutPresenter(): AboutPresenter {
        return SubsCityDagger.component.createAboutPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_about, container, false)
        setSupportActionBar(root.findViewById(R.id.toolbar))
        root.findViewById<View>(R.id.bt_telegram).setOnClickListener { aboutPresenter.openTelegram() }
        root.findViewById<View>(R.id.bt_vk).setOnClickListener { aboutPresenter.openVkontakte() }
        root.findViewById<View>(R.id.bt_fb).setOnClickListener { aboutPresenter.openFacebook() }
        root.findViewById<View>(R.id.bt_review).setOnClickListener { openEmailApp() }
        return root
    }

    override fun openSocialNetwork(url: String) {
        openUrl(Uri.parse(url), false)
    }

    private fun openEmailApp() {
        val intent = IntentUtils.sendEmail(getString(R.string.email_address), getString(R.string.email_subject))
        if (intent.resolveActivity(activity!!.packageManager) != null) {
            startActivity(intent)
        } else {
            toast(getString(R.string.about_no_email_application))
        }
    }

}