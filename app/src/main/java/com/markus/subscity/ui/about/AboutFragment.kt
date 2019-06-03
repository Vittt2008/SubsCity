package com.markus.subscity.ui.about

import android.net.Uri
import android.os.Bundle
import androidx.annotation.StringRes
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.markus.subscity.R
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.analytics
import com.markus.subscity.extensions.openIntent
import com.markus.subscity.extensions.openUrl
import com.markus.subscity.extensions.setSupportActionBar
import com.markus.subscity.utils.ClickableSpanBuilder
import com.markus.subscity.utils.IntentUtils

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
        root.findViewById<View>(R.id.bt_rate_app).setOnClickListener { openEmailApp(R.string.email_address) }
        initTeamMembers(root)
        return root
    }

    override fun openSocialNetwork(socialNetwork: AboutView.SocialNetwork, city: String, url: String) {
        analytics().logOpenSocialNetwork(socialNetwork.toString(), city, url)
        openUrl(Uri.parse(url), false)
    }

    private fun initTeamMembers(root: View) {
        val members = root.findViewById<TextView>(R.id.tv_team_members)
        members.movementMethod = LinkMovementMethod.getInstance()
        val text = ClickableSpanBuilder(requireActivity(), R.string.about_team_members)
                .addLink(R.string.about_team_developer) { openEmailApp(R.string.developer_email) }
                .addLink(R.string.about_team_designer) { openEmailApp(R.string.designer_email) }
                .addLink(R.string.about_team_github) { openGitHub() }
                .build()
        members.text = text
    }

    private fun openEmailApp(@StringRes emailId: Int) {
        val email = getString(emailId)
        analytics().logOpenEmail(email)
        val intent = IntentUtils.createSendEmailIntent(email, getString(R.string.email_subject))
        openIntent(intent, R.string.about_no_email_application)
    }

    private fun openGitHub() {
        analytics().logOpenGitHub()
        openUrl(Uri.parse(getString(R.string.github_url)), false)
    }

}