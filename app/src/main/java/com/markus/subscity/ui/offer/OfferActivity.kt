package com.markus.subscity.ui.offer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.markus.subscity.R
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.openIntent
import com.markus.subscity.ui.main.MainActivity
import com.markus.subscity.utils.IntentUtils

/**
 * @author Vitaliy Markus
 */
class OfferActivity : MvpAppCompatActivity(), OfferView {

    @InjectPresenter
    lateinit var offerPresenter: OfferPresenter

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, OfferActivity::class.java)
            context.startActivity(intent)
        }
    }

    @ProvidePresenter
    fun moviesPresenter(): OfferPresenter {
        return SubsCityDagger.component.createOfferPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer)

        findViewById<Button>(R.id.bt_open_play_store).setOnClickListener { offerPresenter.openPlayStore() }
        findViewById<Button>(R.id.bt_open_application).setOnClickListener {
            MainActivity.start(this)
            finish()
        }
    }

    override fun openPlayStore() {
        val intent = IntentUtils.openPlayStore(this)
        openIntent(intent, R.string.offer_no_play_store)
    }
}