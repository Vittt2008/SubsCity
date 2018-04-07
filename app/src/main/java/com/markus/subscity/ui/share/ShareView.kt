package com.markus.subscity.ui.share

import com.arellomobile.mvp.MvpView
import java.io.File

/**
 * @author Vitaliy Markus
 */
interface ShareView : MvpView {
    fun share(file: File?, title: String, content: String)
}