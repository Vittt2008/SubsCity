package com.source.subscity.ui.about

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.source.subscity.R
import com.source.subscity.extensions.setSupportActionBar

/**
 * @author Vitaliy Markus
 */
class AboutFragment : Fragment() {

    companion object {
        fun newInstance(): AboutFragment {
            return AboutFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_about, container, false)
        setSupportActionBar(root.findViewById(R.id.toolbar))
        return root
    }

}