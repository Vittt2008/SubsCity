package com.markus.subscity.ui.donate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.markus.subscity.R
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.setSupportActionBar

/**
 * @author Vitaliy Markus
 */
class DonateFragment : MvpAppCompatFragment(), DonateView {

    @InjectPresenter
    lateinit var donatePresenter: DonatePresenter

    companion object {
        fun newInstance(): DonateFragment {
            return DonateFragment()
        }
    }

    @ProvidePresenter
    fun donateFragment(): DonatePresenter {
        return SubsCityDagger.component.createDonatePresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_donate, container, false)
        setSupportActionBar(root.findViewById(R.id.toolbar))
        return root
    }

}