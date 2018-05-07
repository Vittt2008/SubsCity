package com.markus.subscity.ui.share

import android.graphics.drawable.Drawable
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.SubsCityApplication
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.utils.DrawableExporter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class SharePresenter @Inject constructor(private val drawableExporter: DrawableExporter,
                                         private val cityProvider: CityProvider) : MvpPresenter<ShareView>() {

    private val POSTER_NAME = "temp_poster.jpg"

    fun share(drawable: Drawable?, movie: Movie) {
        Single.fromCallable { FileOptional(drawableExporter.export(drawable, POSTER_NAME)) }
                .map { file -> ShareData(file.value, formatTitle(movie), String.format(SubsCityApplication.MOVIE_URL, cityProvider.cityId, movie.id)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.share(it.file, it.title, it.url) }, { viewState.onShareError() })
    }

    private fun formatTitle(movie: Movie): String {
        val title = movie.title
        return if (title.russian.isNotEmpty() && title.original.isNotEmpty()) {
            "${title.russian} (${title.original})"
        } else if (title.russian.isNotEmpty()) {
            title.russian
        } else {
            title.original
        }
    }

    class ShareData(val file: File?, val title: String, val url: String)

    class FileOptional(val value: File?)

}
